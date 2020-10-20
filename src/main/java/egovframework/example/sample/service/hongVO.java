package egovframework.example.sample.service;

import java.io.File;
import java.sql.Date;

import org.springframework.web.multipart.MultipartFile;

public class hongVO {
	private int hong_idx; // 일련번호
	private String name; // 이름
	private String image_Path; // 이미지 경로
	private String image_name;// 이미지 이름 
	private String originalFileName; // 원래 파일명
	private Date reg_Date;// 등록일
	private byte[] image; // 이미지
	private int image_size; // 파일 크기
	private MultipartFile imgFile; // 통째로 넣어보기 음핫핫
	 
    public MultipartFile getImgFile() {
        return imgFile;
    }
 
    public void setImgFile(MultipartFile imgFile) {
        this.imgFile = imgFile;
    }

    


	
	public String getOriginalFileName() {
		return originalFileName;
	}

	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
	}

	public int getImage_size() {
		return image_size;
	}
	public void setImage_size(int image_size) {
		this.image_size = image_size;
	}
	public Date getReg_Date() {
		return reg_Date;
	}
	public void setReg_Date(Date reg_Date) {
		this.reg_Date = reg_Date;
	}
	public int gethong_idx() {
		return hong_idx;
	}
	public void sethong_idx(int idx) {
		this.hong_idx = idx;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public byte[] getImage() {
		return image;
	}
	public void setImage(byte[] image) {
		this.image = image;
	}
	public String getImage_Path() {
		return image_Path;
	}
	public void setImage_Path(String image_Path) {
		this.image_Path = image_Path;
	}
	public String getImage_name() {
		return image_name;
	}
	public void setImage_name(String image_name) {
		this.image_name = image_name;
	}
	
	
	
	
}
