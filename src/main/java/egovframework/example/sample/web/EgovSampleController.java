/*
 * Copyright 2008-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package egovframework.example.sample.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import egovframework.example.sample.service.EgovSampleService;
import egovframework.example.sample.service.hongVO;
import egovframework.example.sample.util.FileUtils;
import egovframework.rte.fdl.property.EgovPropertyService;

/**
 * @Class Name : EgovSampleController.java
 * @Description : EgovSample Controller Class
 * @Modification Information
 * @
 * @  수정일      수정자              수정내용
 * @ ---------   ---------   -------------------------------
 * @ 2009.03.16           최초생성
 *
 * @author 개발프레임웍크 실행환경 개발팀
 * @since 2009. 03.16
 * @version 1.0
 * @see
 *
 *  Copyright (C) by MOPAS All right reserved.
 */

@Controller
public class EgovSampleController {
	private static final String image_file = "C:\\test\\";
	private static final String filePath = "C:/Users/bareu/Desktop/test/";
	@Resource(name="fileUtils")
	private FileUtils fileUtils;
	/** EgovSampleService */
	@Resource(name = "sampleService")
	private EgovSampleService sampleService;

	/** EgovPropertyService */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;

	/** Validator */
	//@Resource(name = "beanValidator")
	//protected DefaultBeanValidator beanValidator;
	//private static final Logger logger = LoggerFactory.getLogger(UploadFileUtils.class);
	

	@RequestMapping(value = "/insertHong.do", method = { RequestMethod.POST,RequestMethod.GET }, produces = "text/html; charset=utf8")
	public String test(@ModelAttribute("hongVO") hongVO hongVO,MultipartHttpServletRequest Request) throws Exception {
		
		Request.setCharacterEncoding("utf-8");
		sampleService.insertHongVO(hongVO, Request);
		return "forward:/egovSampleList.do";
	}
	
	/**
	 * 파일 하나씩 다운로드  
	 * @param hongVO - 조회할 정보가 담긴 hongVO - 중 hong_idx 사용
	 * @return "hongVO"
	 * @exception Exception
	 */
	
	@RequestMapping(value="/download.do")
	public void fileDown(@ModelAttribute("hongVO") hongVO vo, HttpServletResponse response) throws Exception{
		
		hongVO result = sampleService.selectOne(vo);
		String storedFileName = result.getImage_Path();
		String originalFileName = result.getImage_name();
		String folder = result.getName();
		
		// 파일을 저장했던 위치에서 첨부파일을 읽어 byte[]형식으로 변환한다.
		byte fileByte[] = org.apache.commons.io.FileUtils.readFileToByteArray(new File(image_file+folder+"/"+originalFileName));
		
		response.setContentType("application/octet-stream");
		response.setContentLength(fileByte.length);
		response.setHeader("Content-Disposition",  "attachment; fileName=\""+URLEncoder.encode(originalFileName, "UTF-8")+"\";");
		response.getOutputStream().write(fileByte);
		response.getOutputStream().flush();
		response.getOutputStream().close();
	}
	/**
	 * 다중 파일  다운로드  
	 * .zip 파일로 저장 할 예정
	 * 
	 *	동작 순서 -  1. DB에 저장된 파일 저장경로를 뽑아서 List로  저장.
	 *           2. 파일을 zip으로 묶는 반복문 	
	 *
	 * @param hongVO - 조회할 정보가 담긴 hongVO - 중 hong_idx 사용
	 * @return "hongVO"
	 * @exception Exception
	 */
	@RequestMapping(value="/downloadZip.do")
	public void fileDownZip(HttpServletRequest request, HttpServletResponse response) throws Exception{
	
		fileUtils.fileDownZip(request, response);
	}
	
	// 누르면 하나씩 출력
	@RequestMapping(value="/getByteImage.do")
	public ResponseEntity<byte[]> getByteImage(@RequestParam int hong_idx) {
		hongVO vo = new hongVO();
		vo.sethong_idx(hong_idx);
		
		byte[] imageContent = null;
		final HttpHeaders headers = new HttpHeaders();
	    List<Map<String, Object>> map;
	    String stored_path ="";
	    
		try {
				vo = sampleService.selectOne(vo);
		    	stored_path = vo.getImage_Path();	
			    byte[] image = fileUtils.imageToByteArray(stored_path);	
			    imageContent = image;
			    headers.setContentType(MediaType.IMAGE_PNG);		       
		   
		    
		} catch (Exception e) {

			e.printStackTrace();
		}
	    return new ResponseEntity<byte[]>(imageContent, headers, HttpStatus.OK);
	}
	

	@RequestMapping(value = "/egovSampleList.do")
	public String selectVO(HttpServletRequest Request,HttpServletResponse Response,Model model) throws Exception {
		
		Request.setCharacterEncoding("utf-8");
		Response.setContentType("image/png");
		Map<String,Object> map = new HashMap<String,Object>();
		
		String filePath = "";
				
		List<Map<String, Object>> hongList = (List<Map<String, Object>>) sampleService.selectListHongVO();
			
		model.addAttribute("HongList", hongList);
		
		return "sample/hongRegister";
	}
	
	@RequestMapping(value = "/remove.do", method = RequestMethod.GET, produces = "text/html; charset=utf8")
	public String deleteVO(@RequestParam int hong_idx, @ModelAttribute("hongVO") hongVO hongVO,HttpServletRequest Request) throws Exception {
		Request.setCharacterEncoding("utf-8");
		System.out.println("삭제 테스트 ...1 " + hong_idx);
		hongVO.sethong_idx(hong_idx);
		sampleService.deleteHongVO(hongVO);
		
		return "forward:/egovSampleList.do";
	}
	
	
	
}
 