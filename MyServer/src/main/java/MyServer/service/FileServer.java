package MyServer.service;

import MyServer.ConstValue;
import MyServer.bean.FileMessage;
import MyServer.bean.Pic;
import MyServer.bean.Style;
import MyServer.bean.User;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.LinkedList;
import java.util.List;

@Service
public class FileServer {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public  FileMessage process(HttpServletRequest request) {
        InputStream is = null;
        try {
            is = request.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            FileMessage fileMessage = new FileMessage();
            fileMessage.setStatus(FileMessage.status_wrong);
            return fileMessage;
        }
        DataInputStream dis = new DataInputStream(is);
        //接受传递过来的用户名与文件名
        String useraccount=request.getParameter("useraccount");
        String modelname=request.getParameter("modelname");


        String result="";
        if(modelname==null||modelname.equals("")){
            System.out.println("无效请求！");
            FileMessage fileMessage = new FileMessage();
            fileMessage.setStatus(FileMessage.status_wrongRequest);
            return fileMessage;


        }else{
            Long ct=System.currentTimeMillis();
            System.out.println("useraccount:"+useraccount+" modelname:"+modelname);
            String s[]=modelname.split("\\.");
            String fileType=s[s.length-1];

            String preFileName=useraccount+ct+".jpg";
            System.out.println("开始接受图片:"+preFileName);
            saveFile(dis,preFileName);

            if(ConstValue.isProcessing){
                //服务器正在处理图片，提示用户稍后重试
                File file = new File(ConstValue.img_dir_pre+preFileName);
                if(file.exists())file.delete();
                FileMessage fileMessage = new FileMessage();
                fileMessage.setStatus(FileMessage.status_wait);
                return fileMessage;
            }
            System.out.println("开始处理图片");
            ConstValue.isProcessing=true;
            if(processPic(preFileName,useraccount+ct,modelname)){
                //处理成功
                addExp(Long.valueOf(useraccount),ConstValue.exp_processpic);

                Style style=search(modelname).get(0);
                if(style!=null&&style.getName().length()>1){
                    insertDB(useraccount,useraccount+ct,style.getName(),get(Long.valueOf(useraccount)).getUsername());
                }else{
                    insertDB(useraccount,useraccount+ct,modelname,get(Long.valueOf(useraccount)).getUsername());
                }


                FileMessage fileMessage = new FileMessage();
                MyServer.bean.File file = new MyServer.bean.File();
                file.setAccount(Long.valueOf(useraccount));
                file.setUsername(useraccount);
                file.setUrlpath(ConstValue.img_url_after+useraccount+ct+".jpg");
                fileMessage.addFile(file);
                ConstValue.isProcessing=false;
                return fileMessage;
            }else{
                //处理失败
                ConstValue.isProcessing=false;
                System.out.println("处理失败");
                FileMessage fileMessage = new FileMessage();
                fileMessage.setStatus(FileMessage.status_wrong);
                return fileMessage;

            }

        }
    }

    /**
     * 保存接收到的文件到本地
     * @param dis,fileName
     * @return 文件的绝对目录
     */
    private String saveFile(DataInputStream dis, String fileName) {
        File pf = new File(ConstValue.img_dir_pre);
        if(!pf.exists()){
            pf.mkdir();
        }
        File file = new File(ConstValue.img_dir_pre+fileName);
        if (file.exists()) {
            file.delete();
        }
        FileOutputStream fps = null;
        try {
            fps = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int length = -1;

        try {
            while ((length = dis.read(buffer)) != -1) {
                fps.write(buffer, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fps.flush();
            fps.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }



    private boolean processPic(String inFileName,String outFileName,String modelname){
        if(modelname==null||modelname.equals("")){
            modelname="la_muse";
        }
        try {
            String[] args1=new String[]{ConstValue.python, "transform.py", "-i",ConstValue.img_dir_pre+inFileName,"-s",modelname, "-b","0.1","-o", ConstValue.img_dir_after+"tmp"};
            Process pr=Runtime.getRuntime().exec(args1,null,new File(ConstValue.transformPath));
            BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
            pr.waitFor();
            Thumbnails.of(ConstValue.img_dir_after+"tmp.jpg")
                    .scale(1f)
                    .outputQuality(0.5f)
                    .toFile(ConstValue.img_dir_after+outFileName+".jpg");
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private int insertDB(String account,String filename,String stylename,String username){
        return jdbcTemplate.update("insert into userfile(account,picname,stylename,username,time) values(?,?,?,?,?)", account,filename+".jpg",stylename,username,System.currentTimeMillis());
    }


    /**
     * 查询用户信息
     * @param account
     * @return
     */
    public User get(Long account) {
        List<User> result = jdbcTemplate.query("select * from userinfo where account = ?", new Object[] {
                account }, new BeanPropertyRowMapper(User.class));
        if (result == null || result.isEmpty()) {
            return null;
        }
        return result.get(0);
    }
    /**
     * 用户经验增加
     * @param account
     * @return
     */
    public User addExp(Long account,int exp) {
        if(get(account)!=null){
            jdbcTemplate.update("update userinfo set exp=exp+?  where account = ?", exp,account);
            return get(account);
        }
        return new User();
    }


    /**
     * 查询用户信息
     * @param account
     * @param password
     * @return
     */
    public User get(Long account, String password) {
        List<User> result = jdbcTemplate.query("select * from userinfo where account = ? and password = ?", new Object[] {
                account,password }, new BeanPropertyRowMapper(User.class));
        if (result == null || result.isEmpty()) {
            return null;
        }
        return result.get(0);
    }


    public List<Style> search(String modelname){
        List<Style> styleList = jdbcTemplate.query("select * from styles where modelname = ?", new Object[] {
                modelname }, new BeanPropertyRowMapper(Style.class));
        for(int i=0;i<styleList.size();i++){
            Style style=styleList.get(i);
            style.setPicurl(ConstValue.img_model_url+style.getPicurl());
            styleList.set(i,style);
        }
        return styleList;
    }
}
