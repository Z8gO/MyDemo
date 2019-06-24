package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

/**
 * 类名:		SFTPUtil
 * 描述:		SFTPUtil
 * @author 	ZHANGHUANG
 * @date 	2019年6月14日 下午3:05:27
 *
 */
public class SFTPUtil implements Serializable {

  private static final long   serialVersionUID = 1L;

  private static final Logger logger           = LoggerFactory.getLogger(SFTPUtil.class);

  private ChannelSftp         sftp;

  /**
   * 连接sftp服务器
   *
   * @param host     主机
   * @param port     端口
   * @param username 用户名
   * @param password 密码
   * @return
   */
  public void connect(String host, int port, String username, String password) {
    try {
      JSch jsch = new JSch();
      jsch.getSession(username, host, port);
      Session sshSession = jsch.getSession(username, host, port);
      sshSession.setPassword(password);
      Properties sshConfig = new Properties();
      sshConfig.put("StrictHostKeyChecking", "no");
      sshSession.setConfig(sshConfig);
      sshSession.connect();
      logger.info("SFTP Session connected.");
      Channel channel = sshSession.openChannel("sftp");
      channel.connect();
      sftp = (ChannelSftp) channel;
      logger.info("Connected to " + host);
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
  }

  /**
   * 上传文件
   *
   * @param directory  上传的目录
   * @param uploadFile 要上传的文件
   */
  public boolean upload(String local, String remote) {
    try {
      String fileDir = remote.substring(0, remote.lastIndexOf("/"));

      try {
        sftp.cd(fileDir);
      } catch (Exception e) {
        logger.info("打开文件夹失败！" + e.getMessage());
        logger.info("准备创建文件夹！");
        sftp.mkdir(fileDir);
        sftp.cd(fileDir);
      }

      FileInputStream fileInputStream = new FileInputStream(new File(local));
      String fileName = remote.substring(remote.lastIndexOf("/") + 1, remote.length());
      logger.info("上传文件名：" + fileName);
      sftp.put(fileInputStream, fileName);
      logger.info("上传文件" + fileName + "成功！");
      fileInputStream.close();
      return true;
    } catch (Exception e) {
      logger.error(e.getMessage());
      return false;
    }
  }

  public void disconnect() {
    try {
      sftp.getSession().disconnect();
    } catch (JSchException e) {
      logger.error(e.getMessage());
    }
    sftp.quit();
    sftp.disconnect();
  }

  public static void main(String[] args) throws SftpException {
    String host = "XXX.XXX.XXX.XXX";
    int port = 22;
    String username = "XXXXXXX";
    String password = "XXXXX";
    String remote = "/home/XXXXXX/downdata/20190614/ftp_pass.txt";
    String remote1 = "/home/XXXXXXX/downdata/20190614/ftp_pass1.txt";
    String local = "E:\\MYSPACE\\Desktop\\ftp_pass.txt";
    
    SFTPUtil sFTPUtil = new SFTPUtil();
    sFTPUtil.connect(host, port, username, password);
    sFTPUtil.upload(local, remote);
    sFTPUtil.upload(local, remote1);
    
    sFTPUtil.disconnect();

  }
}
