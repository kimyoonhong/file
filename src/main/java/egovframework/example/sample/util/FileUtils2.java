package egovframework.example.sample.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import egovframework.example.sample.service.hongVO;
import egovframework.example.sample.web.MediaUtils;

//@Component("fileUtils2")
public class FileUtils2 {
	
		private static final String image_file = "C:\\test\\";
		
		public void delete_file (hongVO vo) {
			// 저장경로 , 경로에 파일 있으면 지운다.		
			String image_Path = vo.getImage_Path();
			String image_name = vo.getImage_name();
			String name = vo.getName();
			System.out.println("delete_file 메소드 객체 생성전 ......................");
			System.out.println("image_Path : " + image_Path);
			System.out.println("image_name : " + image_name);
			System.out.println("name : " + name);
			// 저장경로의 파일 객체 생성
			File file = new File(image_file + name);
			File del_file = new File(image_Path);
			System.out.println((String)del_file.getName()==(String)(image_name));
			System.out.println("file.exists() : " + file.exists());
			System.out.println("file.getName() : " + file.getName());
			System.out.println("del_file.getName() : " + del_file.getName()); 
			
			// 파일이 존재하면
			if(file.exists()) {
				/*if((String)del_file.getName()==(String)(image_name)) {
				//삭제
					del_file.delete();
				}*/
				System.out.println("del_file.delete() : "+del_file.delete());
				System.out.println("del_file.canWrite() : "+del_file.canWrite());
				del_file.delete();
			}else {
				System.out.println("파일삭제 오류");
			}
		}		
		// 파일 데이터 
		public List<Map<String,Object>> parseInsertFileInfo(hongVO hongVO, MultipartHttpServletRequest multipartHttpServletReqeust)throws Exception{
			
			System.out.println("parseInsertFileInfo 메서드 실행 ...");
			Iterator<String> iterator = multipartHttpServletReqeust.getFileNames();
			
			MultipartFile multipartFile = null;
			String originalFileName = null;
			String originalFileExtension = null;
			String storedFileName = null;
			// 입력한 이름의 폴더를 생성한 후 이미지 파일을 저장하기 위한 변수
			String folder = hongVO.getName();
			
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			Map<String, Object> listMap = null;
			
			// 지정경로 + 입력받은 이름폴더 ex c:\test\이름\
			File file = new File(image_file + folder+"\\");
			if(!file.exists()) {
				// 지정경로에 파일이없으면 디렉토리 생성.
				file.mkdirs();
			}
			
			System.out.println("parseInsertFileInfo 메서드 실행 ... while문 전...");
			while (iterator.hasNext()) {
				multipartFile = multipartHttpServletReqeust.getFile(iterator.next());
				if(!multipartFile.isEmpty()) {
					// 원래 파일 명  저장.
					originalFileName = multipartFile.getOriginalFilename();
					// 원래 파일 확장자
					originalFileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
					
					// 이미지 파일 명 변경.
					storedFileName = getRandomString() + originalFileExtension;
					// 해당경로에 랜덤파일명을 적용한 파일 객체생성
					file = new File(image_file +folder +"\\" +storedFileName);
					// 파일 저장
					multipartFile.transferTo(file);

					
					
					// 저장경로 => c:\test\이름\랜덤파일명
					String image_path = image_file +folder+"\\"+FilenameUtils.getName(storedFileName);
					String image_name = FilenameUtils.getName(storedFileName);
					listMap = new HashMap<String, Object>();
					listMap.put("fileNm", originalFileName);
					listMap.put("image_name", image_name);
					listMap.put("image", image_path);					
					listMap.put("image_size", (int)multipartFile.getSize());
					listMap.put("originalFileName", originalFileName);
					list.add(listMap);	
				}
			}
				return list;
		}
		
		public static String getRandomString() {
			return UUID.randomUUID().toString().replaceAll("-", "");
		}
		
		// Blob 데이터 -> byte[] 데이터로 변환하기.
		public byte[] imageToByteArray (String filePath) throws Exception{
			
			System.out.println("imageToByteArray()메서드 실행 ....");
			byte[] returnValue = null;
			
			ByteArrayOutputStream baos = null;
			FileInputStream fis = null;
			
			try{
				baos = new ByteArrayOutputStream();
				fis = new FileInputStream(filePath);
				
				byte[] buf = new byte[1024];
				int read = 0;
				
				while((read=fis.read(buf, 0, buf.length))!= -1 ) {
					baos.write(buf, 0, read);
				}
				
				returnValue = baos.toByteArray();
			} catch (Exception e){
				e.printStackTrace();
			} finally {
				if (baos != null) {baos.close();}
				if (fis != null)  {baos.close();}
			}
			
			return returnValue;
		}
		
		public  void byte2Image(String path, byte[] buffer) throws FileNotFoundException, IOException {
	        FileImageOutputStream imageOutput = new FileImageOutputStream(new File(path));
	        imageOutput.write(buffer, 0, buffer.length);
	        imageOutput.close();
	    }

	

		
		public String makeIcon(String uploadpath, String path,
				String fileName) {
			// TODO Auto-generated method stub
			String iconName = uploadpath + path + File.separator + fileName;		
			
			return iconName.substring(uploadpath.length()).replace(File.separatorChar, '/');
		}

		public String calcPath(String uploadpath){
			Calendar cal = Calendar.getInstance();
			
			String yearPath = File.separator+cal.get(Calendar.YEAR);
			
			String monthPath = yearPath + File.separator + new DecimalFormat("00").format(cal.get(Calendar.MONTH)+1);
			
			String datePath = monthPath + File.separator + new DecimalFormat("00").format(cal.get(Calendar.DATE)+1);
			
			makeDir(uploadpath, yearPath, monthPath, datePath);
			
			//logger.info(datePath);
			
			return datePath;
		}

		public void makeDir(String uploadpath, String... paths) {
			// TODO Auto-generated method stub
			if(new File(paths[paths.length-1]).exists()){
				return;
			}
			
			for (String path : paths){
				File dirPath = new File(uploadpath + path);
				
				if (! dirPath.exists()){
					dirPath.mkdir();
				}
			}
		}
		// 썸네일 생성
		public  String makeThumbnail(String uploadPath, String path, String fileName) throws Exception {
			// 이미지를 읽기 위한 버퍼
			BufferedImage sourceImg = ImageIO.read(new File(uploadPath + path, fileName));
			// 100픽셀 단위의 썸네일 생성
			BufferedImage destImg = Scalr.resize(sourceImg, Scalr.Method.AUTOMATIC, Scalr.Mode.FIT_TO_HEIGHT, 100);
			// 썸네일의 이름을 생성(원본 파일에 's_'를 붙힘
			String thumbnailName = uploadPath + path + File.separator + "s_" + fileName;
			File newFile = new File(thumbnailName);
			// 확장자
			String formatName = fileName.substring(fileName.lastIndexOf(".")+1);
			// 썸네일 생성
			ImageIO.write(destImg, formatName.toUpperCase(), newFile);
			// 썸네일의 이름을 리턴.
			return thumbnailName.substring(uploadPath.length()).replace(File.separatorChar, '/');
		}
		
		public  String uploadFile(String uploadpath, String originalName, byte[] fileData)throws Exception{
			// UUID 발급
			UUID uid = UUID.randomUUID();
			// 저장할 파일명 = UUID + 원본이름
			String saveName = uid.toString() + "_" + originalName;
			// 업로드할 디렉토리(날짜별 폴더 ) 생성
			String savePath = calcPath(uploadpath);
			// 파일 경로(기존의 업로드경로+날짜별 경로), 파일명을 받아 파일 객체 생성
			File target = new File(uploadpath + savePath,saveName);
			// 임시 디렉토리에 업로드된 파일을 지정된 디렉토리로 복사
			FileCopyUtils.copy(fileData, target);
			// 썸네일을 생성하기 위한 파일의 확장자 검사
			String formatName = originalName.substring(originalName.lastIndexOf(".")+1);
			String uploadedFileName = null;
			// 이미지 파일은 썸네일 사용
			if(MediaUtils.getMediaType(formatName) != null){
				// 썸네일 생성
				uploadedFileName = makeThumbnail(uploadpath, savePath, saveName);
				// 나머지는 아이콘
			}else{
				// 아이콘 생성
				uploadedFileName = makeIcon(uploadpath, savePath, saveName);
			}
			
			return uploadedFileName;
		}
		
}
