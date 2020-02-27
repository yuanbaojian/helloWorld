  
/**    
 * @Title: RunCmdUtils.java  
 * @Package com.atoz.mpm.common.utils
 * @Description: TODO(用一句话描述该文件做什么)  
 * @author caicai.gao  
 * @date 2017年9月8日  
 * @version V1.0    
 */  
            
package com.ybj.utils.common;

import java.io.IOException;

  
/**  
 * @ClassName: RunCmdUtils  
 * @Description: java调用cmd执行批处理文件  
 * @author caicai.gao  
 * @date 2017年9月8日  
 *    
 */

public class RunCmdUtils {
	
	/**
	 * @Title: run_cmd  
	 * @Description: java调用cmd执行批处理文件  
	 * @param strcmd    cmd命令行  
	 * @return void    返回类型  
	 * @throws
	 */
	public static void run_cmd(String strcmd) {
	    Runtime rt = Runtime.getRuntime(); //Runtime.getRuntime()返回当前应用程序的Runtime对象
		Process ps = null;  //Process可以控制该子进程的执行或获取该子进程的信息。
	    try {
		    ps = rt.exec(strcmd);   //该对象的exec()方法指示Java虚拟机创建一个子进程执行指定的可执行程序，并返回与该子进程对应的Process对象实例。
		    ps.waitFor();  //等待子进程完成再往下执行。
		} catch (IOException e1) {
		    e1.printStackTrace();
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}

		int i = ps.exitValue();  //接收执行完毕的返回值
	    if (i == 0) {
		    System.out.println("执行完成.");
		} else {
		    System.out.println("执行失败.");
		}

		ps.destroy();  //销毁子进程
		ps = null;   
    }
	
	public static void runExe(String batPath) throws IOException{
		Runtime r = Runtime.getRuntime();
		Process p = null;
		try {
			p = r.exec(batPath);
			//获取进程的标准输入流
			StreamHandler inputStream = new StreamHandler(p.getInputStream());
			inputStream.start();
			//获取进程的标准错误流
			StreamHandler errorStream = new StreamHandler(p.getErrorStream());
			errorStream.start();
//			p.waitFor();
		} catch (Exception e) {
			System.out.println("运行错误:" + e.getMessage());
			e.printStackTrace();   
		} 
	}

	public static void main(String[] args) {

//        String batName = "mybat.bat"; //该bat文件保存在项目目录下，所以无需写出完整路径，如果文件不在项目目录下则需要直接写出文件路径
        String strcmd = "cmd /c start /b " + "mybat.bat" + " modelNo:" + "123" +" auditInfo:" + "1234:312,321:3234";
        run_cmd(strcmd);

    }
	
}
