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
package egovframework.example.sample.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import egovframework.example.sample.service.EgovSampleService;
import egovframework.example.sample.service.SampleDefaultVO;
import egovframework.example.sample.service.SampleVO;
import egovframework.example.sample.service.hongVO;
import egovframework.example.sample.util.FileUtils;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.rte.fdl.idgnr.EgovIdGnrService;

import javax.annotation.Resource;


import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 * @Class Name : EgovSampleServiceImpl.java
 * @Description : Sample Business Implement Class
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

@Service("sampleService")
public class EgovSampleServiceImpl extends EgovAbstractServiceImpl implements EgovSampleService {

	private static final Logger LOGGER = LoggerFactory.getLogger(EgovSampleServiceImpl.class);

	
	@Resource(name="fileUtils")
	private FileUtils fileUtils;
	
	// TODO mybatis 사용
	@Resource(name="sampleMapper")
	private SampleMapper sampleDAO;

	/** ID Generation */
	@Resource(name = "egovIdGnrService")
	private EgovIdGnrService egovIdGnrService;
	
	/**
	 * 이미지 등록한다.
	 * @param vo - 등록할 정보가 담긴 hongVO
	 * @return 등록 결과
	 * @exception Exception
	 */

	public void insertHongVO(hongVO vo, MultipartHttpServletRequest request) throws Exception{
		
		// hongVO + 파일 정보  List에 담기
		List<Map<String,Object>> list = fileUtils.parseInsertFileInfo(vo, request);
		Map<String,Object> map = new HashMap<String,Object>();
		
		int list_size = list.size();	
		// List에 담은 정보를 Map<String,Object>에 담기
		for (int i=0; i<list_size; i++) {
			map = list.get(i);
		}
		// Map에 담긴 이미지 경로를 변수에 저장
		String image_path = (String) map.get("image");
		String image_name = (String) map.get("image_name");
		String originalFileName = (String) map.get("originalFileName");
		int image_size = (int) map.get("image_size");
		System.out.println("이미지 파일 명 : "+image_name);
		System.out.println("이미지 경로 : "+image_path);
		// 이미지파일 -> byte[] 로 변환 -> 자바에서 Blob으로 변환해줌 -> DB저장
		
		byte[] image = fileUtils.imageToByteArray(image_path);
		// byte[] 타입 이미지, String 타입에 입력받은 name, String 타입에 이미지 파일 저장경로
		// map에 저장.
		map.put("image", image);					
		map.put("name", vo.getName());
		map.put("image_Path", image_path);
		map.put("image_name", image_name);
		map.put("image_size", image_size);
		map.put("originalFileName", originalFileName);
		System.out.println("image_name : " + image_name);
		System.out.println(image);
		sampleDAO.insertHongVO(map);
	}
	
	/**
	 * 등록한 이미지를 수정한다.
	 * @param vo - 수정할 정보가 담긴 hongVO
	 * @return void형
	 * @exception Exception
	 */
	public void updateHongVO(hongVO vo) throws Exception{
		sampleDAO.updateHongVO(vo);
	}
	
	/**
	 * 글을 삭제한다.
	 * @param vo - 삭제할 정보가 담긴 hongVO
	 * @return void형
	 * @exception Exception
	 */
	// 삭제 시 저장된 파일을 지우는 것 추가.
	public void deleteHongVO(hongVO vo) throws Exception{
		
		System.out.println("DB 파일정보 삭제 ...");
		vo = selectOne(vo);
		System.out.println("vo.get imagename : " +vo.getImage_name());
		System.out.println("vo.get path : " +vo.getImage_Path());
		System.out.println("vo.get name : " +vo.getName());
		
		fileUtils.delete_file(vo);
		sampleDAO.deleteHongVO(vo);
		/**
		 * 
		 * 1.  select * from hong_tb where image_name = #{image_name} 추가
		 * 2.  파일 삭제 전 vo를 가지고 와서 넘겨준다.
		 * 
		 */
	}
	
	/**
	 * 글을 조회한다.
	 * @param vo - 조회할 정보가 담긴 hongVO
	 * @return int형
	 * @exception Exception
	 */
	public List<Map<String, Object>> selectListHongVO() throws Exception{
		
		List<Map<String, Object>> map =   (List<Map<String, Object>>) sampleDAO.selectListHongVO();
		
		return map;
	}
	
	public hongVO selectOne(hongVO vo) throws Exception{
		
		vo = (hongVO) sampleDAO.selectOne(vo);
		
		return vo;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 글을 등록한다.
	 * @param vo - 등록할 정보가 담긴 SampleVO
	 * @return 등록 결과
	 * @exception Exception
	 */
	@Override
	public String insertSample(SampleVO vo) throws Exception {
		LOGGER.debug(vo.toString());

		/** ID Generation Service */
		String id = egovIdGnrService.getNextStringId();
		vo.setId(id);
		LOGGER.debug(vo.toString());

		sampleDAO.insertSample(vo);
		return id;
	}

	/**
	 * 글을 수정한다.
	 * @param vo - 수정할 정보가 담긴 SampleVO
	 * @return void형
	 * @exception Exception
	 */
	@Override
	public void updateSample(SampleVO vo) throws Exception {
		sampleDAO.updateSample(vo);
	}

	/**
	 * 글을 삭제한다.
	 * @param vo - 삭제할 정보가 담긴 SampleVO
	 * @return void형
	 * @exception Exception
	 */
	@Override
	public void deleteSample(SampleVO vo) throws Exception {
		sampleDAO.deleteSample(vo);
	}

	/**
	 * 글을 조회한다.
	 * @param vo - 조회할 정보가 담긴 SampleVO
	 * @return 조회한 글
	 * @exception Exception
	 */
	@Override
	public SampleVO selectSample(SampleVO vo) throws Exception {
		SampleVO resultVO = sampleDAO.selectSample(vo);
		if (resultVO == null)
			throw processException("info.nodata.msg");
		return resultVO;
	}

	/**
	 * 글 목록을 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return 글 목록
	 * @exception Exception
	 */
	@Override
	public List<?> selectSampleList(SampleDefaultVO searchVO) throws Exception {
		return sampleDAO.selectSampleList(searchVO);
	}

	/**
	 * 글 총 갯수를 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return 글 총 갯수
	 * @exception
	 */
	@Override
	public int selectSampleListTotCnt(SampleDefaultVO searchVO) {
		return sampleDAO.selectSampleListTotCnt(searchVO);
	}

}
