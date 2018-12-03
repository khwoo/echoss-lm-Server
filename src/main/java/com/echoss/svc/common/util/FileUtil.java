package com.echoss.svc.common.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.onetwocm.aws.S3Handler;

@Component
public class FileUtil {

	private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);
	
	@Value("#{config['s3.upload.bucket']}")
	String s3UploadBucket;
	
	@Value("#{config['s3.upload.url']}")
	String s3UploadUrl;
	
	@Value("#{config['s3.upload.path']}")
	String s3UploadPath;
	
	@Value("#{config['s3.access.key']}")
	String accessKey;
	
	@Value("#{config['s3.secret.key']}")
	String secretKey;
	
	@Value("#{config['file.upload.path']}")
	String fileUploadPath;
	
	/**
	 * S3에 이미지 업로드 (wechat 이미지 등록 시 사용)
	 * @param originFileName 
	 * @param brandImgFile
	 * @return
	 */
	public TAData s3ImangeUpload_wechatImage(MultipartFile brandImgFile, String subDir, String name) throws Exception {
		String ext = brandImgFile.getOriginalFilename().substring(brandImgFile.getOriginalFilename().lastIndexOf("."));
		String fileNm = name + ext;
		
		File dir = new File(fileUploadPath + subDir);
		dir.mkdirs();
		File file = new File(fileUploadPath + subDir +fileNm);
		brandImgFile.transferTo(file);
		S3Handler handler = new S3Handler();
		handler.connect(accessKey, secretKey, Regions.AP_NORTHEAST_2);
		handler.upload(s3UploadBucket, s3UploadPath + subDir + fileNm, file, true);
		TAData param = new TAData();
		param.set("s3Image", s3UploadUrl + subDir + fileNm);
		param.set("wechatImage", file);
		//return s3UploadUrl + subDir + fileNm;
		return param;
	}
	
	/**
	 * S3에 이미지 업로드
	 * @param originFileName 
	 * @param brandImgFile
	 * @return
	 */
	public String s3ImangeUpload(MultipartFile brandImgFile, String subDir, String name) throws Exception {
		String ext = brandImgFile.getOriginalFilename().substring(brandImgFile.getOriginalFilename().lastIndexOf("."));
		String fileNm = name + ext;
		
		File dir = new File(fileUploadPath + subDir);
		dir.mkdirs();
		File file = new File(fileUploadPath + subDir +fileNm);
		
		if(file.exists()) {
			file.delete();
    		logger.debug("이미지[" + file.getAbsolutePath() + "]가 이미 등록되있어 삭제처리합니다.");
    	}
		
		brandImgFile.transferTo(file);
		S3Handler handler = new S3Handler();
		handler.connect(accessKey, secretKey, Regions.AP_NORTHEAST_2);
		handler.upload(s3UploadBucket, s3UploadPath + subDir + fileNm, file, true);
		
		logger.debug("filePath : [" + fileUploadPath + subDir +fileNm + "]");
		logger.debug("s3Upload : [" + s3UploadUrl + "]");
		
		return s3UploadUrl + subDir + fileNm;
	}
	
	private String getImgPath(String subDir) throws Exception {

		String imgFileName = subDir + "/" + Calendar.getInstance().getTimeInMillis() + ".png";

		return imgFileName;
	}
	
	private String getThumbImgPath(String originFilePath) {

		String thumbnailFilePath = originFilePath.substring(0, originFilePath.lastIndexOf(".")) + "_thumb"
				+ originFilePath.substring(originFilePath.lastIndexOf("."));

		return thumbnailFilePath;
	}
	
	// S3업로드 기본
	private String uploadS3Default(String uploadFilePath, File file) {

		AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

		AmazonS3 s3client = new AmazonS3Client(credentials);
		s3client.setRegion(Region.getRegion(Regions.AP_NORTHEAST_2));

		PutObjectRequest putRequest = new PutObjectRequest(s3UploadBucket, s3UploadPath + uploadFilePath, file);
		putRequest.withCannedAcl(CannedAccessControlList.PublicRead);

		// contenttype png
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType("image/jpeg");

		putRequest.withMetadata(metadata);

		// start upload
		s3client.putObject(putRequest);

		String fileURL = s3UploadUrl +  uploadFilePath;

		return fileURL;
	}
	
	public String uploadS3WithResize(String objectId, File file, int width, int height)
			throws Exception {

		return uploadS3WithResize(objectId, file, width, height, 0, 0);
	}
	
	/**
	 * 공통 사용 업로드 메서드 내부에서 이미지 경로를 설정하고, 썸네일을 생성하고 업로드한다.
	 * 
	 * 
	 * @param bucketName
	 * @param objectId
	 *            이미지 파일의 ID, 이미지 경로에 사용된다. <br>
	 *            예를들어 매장 사진일 경우 EWCT_BR_BASE의 BR_ID, 메뉴일 경우, EWCT_BR_MENU의
	 *            MENU_ID
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public String uploadS3WithResize(String objectId, File file, int width, int height, int thumbWidth,
			int thumbHeight) throws Exception {

		File defaultFile = new File(System.getProperty("catalina.base") + "/upload/temp/default" + file.getName());
		File thumbFile = new File(System.getProperty("catalina.base") + "/upload/temp/thumb" + file.getName());
		defaultFile.mkdirs();
		thumbFile.mkdirs();

		String s3URL = "";
		try {

			// 경로 설정
			String uploadFilePath = getImgPath(objectId);
			// 기본 사이즈로 변경
			resizeImageWithScalr(file, defaultFile, width, height);
			// 업로드 및 이미지 주소 반환
			s3URL = uploadS3Default(uploadFilePath, defaultFile);

			if (thumbWidth > 0 && thumbHeight > 0) {

				// 썸네일 경로 설정
				String uploadThumbPath = getThumbImgPath(uploadFilePath);
				// 썸네일 생성
				// resizeImageWithScalr(file, thumbFile, THUMBNAIL_WIDTH,
				// THUMBNAIL_WIDTH);
				resizeImageWithScalr(file, thumbFile, thumbWidth, thumbHeight);
				// 썸네일 업로드 및 주소 반환
				uploadS3Default(uploadThumbPath, thumbFile);
			}

		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if (defaultFile != null) {
				try {
					defaultFile.delete();
				} catch (Exception e) {
				}
			}
			if (thumbFile != null) {
				try {
					thumbFile.delete();
				} catch (Exception e) {
				}
			}
		}

		// CDN주소로 변환
		return s3URL;
	}
	
	public File resizeImageWithScalr(File input, File output, int thumbnailWidth, int thumbnailHeight)
			throws IOException, InterruptedException, NullPointerException {

		BufferedImage originalImage = ImageIO.read(input);

		// int imgwidth = Math.min(originalImage.getHeight(),
		// originalImage.getWidth());
		// int imgheight = imgwidth;

		int imgwidth = Math.min(originalImage.getHeight(), originalImage.getWidth());
		int imgheight = (int) (imgwidth * ((double) thumbnailHeight / thumbnailWidth));

		BufferedImage scaledImage = Scalr.crop(originalImage, (originalImage.getWidth() - imgwidth) / 2,
				(originalImage.getHeight() - imgheight) / 2, imgwidth, imgheight);
		// BufferedImage scaledImage = Scalr.crop(originalImage, thumbnailWidth,
		// thumbnailHeight);

		BufferedImage resizedImage = Scalr.resize(scaledImage, thumbnailWidth, thumbnailHeight);

		ImageIO.write(resizedImage, "png", output);

		return output;
	}
}
