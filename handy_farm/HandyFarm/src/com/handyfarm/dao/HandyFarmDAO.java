package com.handyfarm.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.*;
import java.util.ArrayList;
import java.text.SimpleDateFormat;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.handyfarm.entity.HandyFarmDTO;

public class HandyFarmDAO {
   // 데이터베이스 연결 정보
   DataSource ds;
   Connection con = null;
   PreparedStatement pstmt = null;
   ResultSet rs = null;
      
   // DB 연결하는 기본 생성자
   public HandyFarmDAO() {
      try { // jsp 단위 DB 연결
         Context ctx = new InitialContext();
         ds = (DataSource) ctx.lookup("java:comp/env/jdbc/mariadb");
      } catch(Exception e) {
         try { // java 단위 DB 연결
            Class.forName ("org.mariadb.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mariadb://localhost:3306/handyfarm", "farmplant", "handyfarm");
         } catch(Exception e2) {
            e2.printStackTrace();
         }
      }
   }

   /**
	 * @author 김연주
	 * email : sym61503@naver.com
	 */
	
	//작물별 경작조건, Crop리스트 가져오기 김연주 -----이미지도 DB에 넣고 추가해줘야 됌.
	public ArrayList<HandyFarmDTO> cropAll_Select(String _phone_number) {
		// list 선언
		ArrayList<HandyFarmDTO> list = new ArrayList<HandyFarmDTO>();
		
		try {
			// DB 연결
			con = ds.getConnection();
			
			// 병충해 목록을 가져오는 sql문
			String query = "SELECT c.crops_name, w.wish FROM crops c, wish w WHERE c.cultivar_number=w.cultivar_number AND phone_number=?;";
			
			pstmt = con.prepareStatement(query);

			// 매개변수 값 대입 -> set 메서드에 값 설정
			pstmt.setString(1, _phone_number);

			// sql문 실행
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				
				// 레코드의 정보를 각 변수에 저장
				String crops_name = rs.getString("crops_name");
				Boolean wish = rs.getBoolean("wish");
					
				// data 객체 선언
				HandyFarmDTO data = new HandyFarmDTO();
				
				// data  객체의 set 메서드에 해당하는 값을 설정
				data.setCrops_name(crops_name);
				data.setWish(wish);
				
				
				if (wish == true) { // 찜하기 선택이 되었다면
					data.setImg("../icon/like_heart.png");
				} else { // 찜하기 선택이 안되어있다면
					data.setImg("../icon/unlike_heart.png");
				}
				
				
				list.add(data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (con != null) { con.close(); }
				if (pstmt != null) { pstmt.close(); }
				if (rs != null) { rs.close(); }
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} // end finally
		// 조회 결과 List 반환
		
		return list;
	} // end cropAll_Select
	
	
	// 실시간 병충해 정보 Tip리스트 가져오기 김연주
	public ArrayList<HandyFarmDTO> realInfo_pest_Select(String _category) {
		// list 선언
		ArrayList<HandyFarmDTO> list = new ArrayList<HandyFarmDTO>();
		
		try {
			// DB 연결
			con = ds.getConnection();
			
			// 병충해 목록을 가져오는 sql문
			String query = "SELECT * " + 
						   "FROM real_time_info " + 
						   "WHERE category=?";
			pstmt = con.prepareStatement(query);
			
			// 매개변수 값 대입 -> set 메서드에 값 설정
			pstmt.setString(1, _category);
			
			// sql문 실행
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				// 레코드의 정보를 각 변수에 저장
				String link = rs.getString("link");
				int views = rs.getInt("views");
				String title = rs.getString("title");
				String content = rs.getString("content");
				Date date = rs.getDate("date");
				String img = rs.getString("img");
				
				
				// data 객체 선언
				HandyFarmDTO data = new HandyFarmDTO();
				
				// data  객체의 set 메서드에 해당하는 값을 설정
				data.setLink(link);
				data.setViews(views);
				data.setTitle(title);
				data.setContent(content);
				data.setDate(date);
				
				if (img != null) { // DB에 저장된 사진이 존재하면 해당 사진 가져오기
					data.setImg(img);
				} else { // DB에 저장된 사진이 없으면 HandyFarm Logo 사진 가져오기
					data.setImg("../icon/handyfarm_logo.png");
				}
				
				list.add(data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (con != null) { con.close(); }
				if (pstmt != null) { pstmt.close(); }
				if (rs != null) { rs.close(); }
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} // end finally
		// 조회 결과 List 반환
		
		return list;
	} // end realInfo_pest_Select

   /**
    * @author 정민정
    * email : as514188@gmail.com
    */
   
   // robo_insert   정민정
   public void roboinsert(String _robo_serial, String _robo_img, String _robo_nickname, String _crops_id, String _gh_id, String _id) {
      try {
         con = ds.getConnection();
         String query="INSERT INTO robo(robo_serial, robo_img, robo_nickname, crops_id, gh_id, id)" +
         "VALUES (?,?,?,?,?,?)";
         
         pstmt=con.prepareStatement(query);
         pstmt.setString(1, _robo_serial);
         pstmt.setString(2, _robo_img);
         pstmt.setString(3, _robo_nickname);
         pstmt.setString(4, _crops_id);
         pstmt.setString(5, _gh_id);
         pstmt.setString(6, _id);
         
         int n = pstmt.executeUpdate();
         
      }catch(Exception e) {
         e.printStackTrace();
      }
      
      finally {
         try {
            if(pstmt != null) pstmt.close();
            }
         catch(SQLException e) {
            e.printStackTrace();
         }
      }
   }//robo_insert end
   
   //cultivar_list 정민정
   public ArrayList<HandyFarmDTO> crops_list(String robo_serial){
      ArrayList<HandyFarmDTO> crops_list = new ArrayList<HandyFarmDTO> ();
      
      try {
         con = ds.getConnection();
         String query = "SELECT crops_name, crops_id FROM crops where crops_id <> (SELECT crops_id FROM robo where robo_serial = ?)";
         pstmt = con.prepareStatement(query);
         pstmt.setString(1, robo_serial);
         rs = pstmt.executeQuery();
         
         while(rs.next()) {
            String crops_name = rs.getString("crops_name");
            String crops_id = rs.getString("crops_id");
            //데이터 객체 생성
            HandyFarmDTO data = new HandyFarmDTO();
            data.setCrops_name(crops_name);
            data.setCrops_id(crops_id);
            //리스트 값 추가
            crops_list.add(data);
         }
      }catch(Exception e) {
         e.printStackTrace();
      } finally {
         try {
            if (rs != null)
               rs.close();
            if (pstmt != null)
               pstmt.close();
            if(con != null)
               con.close();
         } catch (SQLException e)  {
            e.printStackTrace();
         }
      }
      System.out.println(crops_list);
      return crops_list;
   }
   //cultivar_list end
   
   //crops_list_robo_insert 위의 쿼리 함수에서 경우에 따라 조금만 바꿔서 해당 함수 쓰려했는데 시간 없어서 걍 새로 만듦
   //정민정
   public ArrayList<HandyFarmDTO> crops_list_insert(){
      ArrayList<HandyFarmDTO> crops_list = new ArrayList<HandyFarmDTO> ();
      
      try {
         con = ds.getConnection();
         String query = "SELECT crops_name, crops_id FROM crops";
         pstmt = con.prepareStatement(query);
         rs = pstmt.executeQuery();
         
         while(rs.next()) {
            String crops_name = rs.getString("crops_name");
            String crops_id = rs.getString("crops_id");
            //데이터 객체 생성
            HandyFarmDTO data = new HandyFarmDTO();
            data.setCrops_name(crops_name);
            data.setCrops_id(crops_id);
            //리스트 값 추가
            crops_list.add(data);
         }
      }catch(Exception e) {
         e.printStackTrace();
      } finally {
         try {
            if (rs != null)
               rs.close();
            if (pstmt != null)
               pstmt.close();
            if(con != null)
               con.close();
         } catch (SQLException e)  {
            e.printStackTrace();
         }
      }
      System.out.println(crops_list);
      return crops_list;
   }
   //cultivar_list_insert end
   
   //robo_select 정민정
   public ArrayList<HandyFarmDTO> robo_list(String gh_id){
      ArrayList<HandyFarmDTO> robo_list = new ArrayList<HandyFarmDTO> ();
      
      try {
         con = ds.getConnection();

         String query = "SELECT robo_nickname, robo_serial, crops_id FROM robo where gh_id = ?";
         pstmt = con.prepareStatement(query);
         pstmt.setString(1, gh_id);
         rs = pstmt.executeQuery();
               
         while(rs.next()) {
            String robo_nickname = rs.getString("robo_nickname");
            String robo_serial = rs.getString("robo_serial");
            String crops_id = rs.getString("crops_id"); 
            //데이터 객체 생성
            HandyFarmDTO data = new HandyFarmDTO();
            data.setRobo_nickname(robo_nickname);
            data.setRobo_serial(robo_serial);
            data.setCrops_id(crops_id);
            //리스트 값 추가
            robo_list.add(data);
         }
      }catch(Exception e) {
         e.printStackTrace();
      } finally {
         try {
            if (rs != null)
               rs.close();
            if (pstmt != null)
               pstmt.close();
            if(con != null)
               con.close();
         } catch (SQLException e)  {
            e.printStackTrace();
         }
      }
      System.out.println(robo_list);
      return robo_list;
   }
   //robo_select end   
         
   //robo_search 정민정
   public ArrayList<HandyFarmDTO> robo_search_list(String robo_serial) {
      
      ArrayList<HandyFarmDTO> robo_search_list = new ArrayList<HandyFarmDTO> ();
      
      try {
         con=ds.getConnection();
         String query="SELECT * FROM robo AS r JOIN crops AS c ON r.crops_id = c.crops_id WHERE robo_serial = ? ";
         pstmt = con.prepareStatement(query);
         pstmt.setString(1, robo_serial);
         rs = pstmt.executeQuery();
         
         while (rs.next()) {
            String robo_nickname = rs.getString("robo_nickname");
            String robo_img = rs.getString("robo_img");
            String crops_name = rs.getString("crops_name");
            String crops_id = rs.getString("crops_id");
            
            HandyFarmDTO data = new HandyFarmDTO();
            data.setRobo_nickname(robo_nickname);
            data.setRobo_img(robo_img);
            data.setCrops_name(crops_name);
            data.setCrops_id(crops_id);
            
            //리스트 값 추가
            robo_search_list.add(data);
         }
         
      }catch(Exception e) {
         e.printStackTrace();
      }
      finally {
      try {
         if(pstmt != null) pstmt.close();
      }catch(SQLException e) {
         e.printStackTrace();
         }
      }
      return robo_search_list;
   }
   //robo_search end
      
   //robo_Update 정민정
   public void robo_update(String robo_serial, String robo_nickname, String robo_img, String crops_id) {
      
      try {
         con = ds.getConnection();
         String query = "UPDATE robo SET robo_nickname = ? , robo_img = ?, crops_id = ? where robo_serial = ?";
         pstmt = con.prepareStatement(query);
         pstmt.setString(1, robo_nickname);
         pstmt.setString(2, robo_img);
         pstmt.setString(3, crops_id);
         pstmt.setString(4, robo_serial);
         int n = pstmt.executeUpdate();
         
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         try {
            if(pstmt != null)
               pstmt.close();
            if(con != null)
               con.close();
         }catch (SQLException e) {
            e.printStackTrace();
         }
      }
   }//robo_Update end
      
   //robo_delete 정민정
   
   public void robo_delete(String robo_serial) {
      try {
         con = ds.getConnection();
         String query = "DELETE FROM robo WHERE robo_serial = ?";
         pstmt = con.prepareStatement(query);
         pstmt.setString(1, robo_serial);
         int n = pstmt.executeUpdate();
      }catch(Exception e) {
         e.printStackTrace();
      } finally {
         try {
         if (pstmt != null)
            pstmt.close();
         if (con != null)
            con.close();
      } catch(SQLException e) {
         e.printStackTrace();
         }
      }
   }
      
   //robo_delete end
      
   //push_log 정민정
   public ArrayList<HandyFarmDTO> push_list(String id){
      ArrayList<HandyFarmDTO> crops_list = new ArrayList<HandyFarmDTO> ();
      ResultSet rs2 = null;
      
      try {
         con = ds.getConnection();
               
         String query = "SELECT * FROM push_log where id = ? ORDER BY push_date DESC";
         pstmt = con.prepareStatement(query);
         pstmt.setString(1, id);
         rs = pstmt.executeQuery();
         
         while(rs.next()) {
            String push_category = rs.getString("push_category");
            String push_msg = rs.getString("push_msg");
            Timestamp push_date = rs.getTimestamp("push_date");
            boolean read_status = rs.getBoolean("read_status");
            String crops_id = rs.getString("crops_id");
            String gh_id = rs.getString("gh_id");
            //데이터 객체 생성0
            HandyFarmDTO data = new HandyFarmDTO();
                  
            data.setPush_category(push_category);
            data.setPush_msg(push_msg);
            data.setPush_date(push_date);
            data.setRead_status(read_status);
            data.setCrops_id(crops_id);
            data.setGh_id(gh_id);

            try{
               String query1 = "SELECT gh_nickname FROM greenhouse where gh_id = ?";
               pstmt = con.prepareStatement(query1);
               pstmt.setString(1, gh_id);
               rs2 = pstmt.executeQuery();
                     
               if(rs2.next()) {
                  String gh_nickname = rs2.getString("gh_nickname");
                  data.setGh_nickname(gh_nickname);
                  System.out.println(gh_nickname);
               }
                     
            }catch(Exception e) {
               e.printStackTrace();
            }
                  
            try{
               String query1 = "SELECT crops_name FROM crops where crops_id = ?";
               pstmt = con.prepareStatement(query1);
               pstmt.setString(1, crops_id);
               rs2 = pstmt.executeQuery();
                     
               if(rs2.next()) {
                  String crops_name = rs2.getString("crops_name");
                  data.setCrops_name(crops_name);
               }
                     
            }catch(Exception e) {
               e.printStackTrace();
            }
            System.out.println(push_category);
                  
            if(push_category.equals("병충해알림")) {
               data.setPush_name("pests");
            }else if(push_category.equals("날씨알림")) {
               data.setPush_name("weathernotifi");
            }else if(push_category.equals("생장정보")) {
               data.setPush_name("growthinfo");
            }else if(push_category.equals("실시간정보")) {
               data.setPush_name("RTinfo");
            }

            //리스트 값 추가
            crops_list.add(data);
         }
      }catch(Exception e) {
         e.printStackTrace();
      } finally {
         try {
            if (rs != null)
               rs.close();
            if (pstmt != null)
               pstmt.close();
            if(con != null)
               con.close();
         } catch (SQLException e)  {
            e.printStackTrace();
         }
      }
      System.out.println(crops_list);
      return crops_list;
   }
   //push_log end
   
   //gh_insert 정민정
   public void gh_insert(String gh_img, String gh_nickname, String id) {
      
      try {
         int count = 0;
         String gh_id_in="";
         String gh_id = "";
         con = ds.getConnection();
         try{
            String query1 = "SELECT count(*) FROM greenhouse";
            pstmt = con.prepareStatement(query1);
            rs = pstmt.executeQuery();
                  
            while(rs.next()) {
               count = rs.getInt(1);
            }
            
            if(count == 0) {
               gh_id = "1";
            }else {
               
               try {
                  String query2 = "SELECT gh_id FROM greenhouse WHERE id = ? order by gh_id desc";
                  pstmt = con.prepareStatement(query2);
                  pstmt.setString(1,  id);
                  rs = pstmt.executeQuery();
                  
                  if(rs.next()) {
                     gh_id_in = rs.getString(1);
                     count = Integer.parseInt(gh_id_in.substring(12));
                     count++;
                     gh_id = Integer.toString(count);
                     System.out.println("1이 아닐때: "+gh_id);
                  }
                  
               }catch(Exception e) {
                  e.printStackTrace();
               }
               
            }
                  
         }catch(Exception e) {
            e.printStackTrace();
         }
         
         String query="INSERT INTO greenhouse (gh_id, gh_img, gh_nickname, id)" +
         "VALUES (?,?,?,?)";

         pstmt=con.prepareStatement(query);
         pstmt.setString(1, "gh-"+ id + "-" + gh_id);
         pstmt.setString(2, gh_img);
         pstmt.setString(3, gh_nickname);
         pstmt.setString(4, id);
         
         int n = pstmt.executeUpdate();
         
      }catch(Exception e) {
         e.printStackTrace();
      }
      
      finally {
         try {
            if (rs != null)
               rs.close();
            if (pstmt != null)
               pstmt.close();
            if(con != null)
               con.close();
         } catch (SQLException e)  {
            e.printStackTrace();
         }
      }
   }//gh_insert_end
   
   //gh_id 가져오기 정민정
   
   public String getGHId(String gh_nickname) {
      String gh_id = null;
      try {
         con = ds.getConnection();
         String query = "SELECT gh_id FROM greenhouse WHERE gh_nickname = ?";
         
         pstmt = con.prepareStatement(query);
         pstmt.setString(1,  gh_nickname);
         rs = pstmt.executeQuery();
         
         if (rs.next()) {
            gh_id = rs.getString("gh_id");
         }
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         try {
            if (rs != null)
               rs.close();
            if (pstmt != null)
               pstmt.close();
            if(con != null)
               con.close();
         } catch (SQLException e)  {
            e.printStackTrace();
         }
      }
      return gh_id;
   }//gh_id 가져오기 end
   
   
   //gh_in_list 정민정
   public ArrayList<HandyFarmDTO> gh_in_list(String gh_id) {
      
      ArrayList<HandyFarmDTO> robo_search_list = new ArrayList<HandyFarmDTO> ();
      
      try {
         con=ds.getConnection();
         String query="SELECT * FROM greenhouse WHERE gh_id = ? ";
         pstmt = con.prepareStatement(query);
         pstmt.setString(1, gh_id);
         rs = pstmt.executeQuery();
         
         while (rs.next()) {
            String gh_nickname = rs.getString("gh_nickname");
            String gh_img = rs.getString("gh_img");
            String id = rs.getString("id");
            
            HandyFarmDTO data = new HandyFarmDTO();
            data.setGh_id(gh_id);
            data.setGh_img(gh_img);
            data.setGh_nickname(gh_nickname);
            data.setId(id);
            
            //리스트 값 추가
            robo_search_list.add(data);
         }
         
      }catch(Exception e) {
         e.printStackTrace();
      }
      finally {
      try {
         if(pstmt != null) pstmt.close();
      }catch(SQLException e) {
         e.printStackTrace();
         }
      }
      return robo_search_list;
   }//gh_in_list end


   //gh_update 정민정

   public void gh_update(String gh_id, String gh_nickname, String gh_img) {
   
      try {
         con = ds.getConnection();
         String query = "UPDATE robo SET gh_img = ?, gh_nickname = ? where robo_serial = ?";
         pstmt = con.prepareStatement(query);
         pstmt.setString(1, gh_img);
         pstmt.setString(2, gh_nickname);
         pstmt.setString(3, gh_id);
         int n = pstmt.executeUpdate();
         
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         try {
            if(pstmt != null)
               pstmt.close();
            if(con != null)
               con.close();
         }catch (SQLException e) {
            e.printStackTrace();
         }
      }
   }

   //gh_update end
   
   
   //gh_delete 정민정
   
   public void gh_delete(String gh_id) {
      try {
         con = ds.getConnection();
         String query = "DELETE FROM greenhouse WHERE gh_id = ?";
         pstmt = con.prepareStatement(query);
         pstmt.setString(1, gh_id);
         int n = pstmt.executeUpdate();
      }catch(Exception e) {
         e.printStackTrace();
      } finally {
         try {
         if (pstmt != null)
            pstmt.close();
         if (con != null)
            con.close();
      } catch(SQLException e) {
         e.printStackTrace();
         }
      }
   }
      
   //gh_delete end
   

	/**
	 * @author 임예나
	 * email : yenaim0108@gmail.com
	 */

	// 수확 가능 비율 DB에 저장하기 임예나
	public void insertHarvestable(Timestamp _time, String _serial, float _harvestable) {
		// datas, robo 선언
		String[] datas = new String[3];
		String robo = null;
		
		// time 변수에서 년월일, 시분초 나눠서 가져오기
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HHmmss");
		String d = sdf.format(_time);
		String[] date = d.split(" ");
		
		try {
			// 로보가 속한 품종번호, 온실 ID, 휴대폰 번호 가져오는 sql문
			String query = "SELECT crops_id, gh_id, id " + 
						   "FROM robo " + 
						   "WHERE robo_serial = ?";
			
			pstmt = con.prepareStatement(query);
			// 매개변수 값 대입 -> set 메서드에 값 설정
			pstmt.setString(1, _serial);
			// sql문 실행
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				// datas 배열의 값 설정
				datas[0] = rs.getString("crops_id");
				datas[1] = rs.getString("gh_id");
				datas[2] = rs.getString("id");
			}
			
			// harvestable 테이블에 데이터를 넣는 sql문
			query = "INSERT INTO harvestable (hrv_id, harvestable, upload_time, robo_serial, crops_id, gh_id, id) " + 
					"VALUES (?, ?, ?, ?, ?, ?, ?)";
			
			pstmt = con.prepareStatement(query);
			// 매개변수 값 대입 -> set 메서드에 값 설정
			pstmt.setString(1, "hrv-" + _serial + "-" + date[0] + "-" + date[1]);
			pstmt.setFloat(2, _harvestable);
			pstmt.setTimestamp(3, _time);
			pstmt.setString(4, _serial);
			pstmt.setString(5, datas[0]);
			pstmt.setString(6, datas[1]);
			pstmt.setString(7, datas[2]);
			// sql문 적용
			pstmt.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		finally {
			try {
				if (con != null) { con.close(); }
				if (pstmt != null) { pstmt.close(); }
				if (rs != null) { rs.close(); }
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} // end finally
	} // end insertHarvestable
	
	// 센서값 DB에 저장하기 임예나
	public void insertSensorValue(Timestamp _time, String _serial, String _sensor_type, float _sensor_value) {
		// datas, sensor, count 선언
		String[] datas = new String[4];
		String sensor = null;
		int count = 0;
		
		try {
			// sensor_id를 가져오는 sql문
			String query = "SELECT sensor_id " + 
						   "FROM sensor " + 
						   "WHERE robo_serial = ? AND sensor_type = ?";
		
			pstmt = con.prepareStatement(query);
			// 매개변수 값 대입 -> set 메서드에 값 설정
			pstmt.setString(1, _serial);
			pstmt.setString(2, _sensor_type);
			// sql문 실행
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				// datas 배열의 값 설정
				datas[0] = rs.getString("sensor_id");
				
				// 센서 ID 뒷 3자리 가져오기
				sensor = datas[0].substring(datas[0].length() - 3, datas[0].length());
			}
						
			// 로보가 속한 품종번호, 온실 ID, 휴대폰 번호 가져오는 sql문
			query = "SELECT crops_id, gh_id, id " + 
					"FROM robo " + 
					"WHERE robo_serial = ?";
			
			pstmt = con.prepareStatement(query);
			// 매개변수 값 대입 -> set 메서드에 값 설정
			pstmt.setString(1, _serial);
			// sql문 실행
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				// datas 배열의 값 설정
				datas[1] = rs.getString("crops_id");
				datas[2] = rs.getString("gh_id");
				datas[3] = rs.getString("id");				
			}
			
			// sensor_value 갯수를 가져오는 sql문
			query = "SELECT COUNT(*) " + 
					"FROM sensor_value " + 
					"WHERE sensor_id = ?";
		
			pstmt = con.prepareStatement(query);
			// 매개변수 값 대입 -> set 메서드에 값 설정
			pstmt.setString(1, datas[0]);
			// sql문 실행
			rs = pstmt.executeQuery();
			
			if (rs.next()) { // 행이 있으면 갯수 + 1 가져오기
				count = rs.getInt(1) + 1;
			} else { // 행이 없으면 1 지정하기
				count = 1;
			}
			
			// sensor_value를 테이블에 데이터를 넣는 sql문
			query = "INSERT INTO sensor_value (value_id, sensor_value, measure_time, sensor_id, robo_serial, crops_id, gh_id, id) " + 
					"VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
			
			pstmt = con.prepareStatement(query);
			// 매개변수 값 대입 -> set 메서드에 값 설정
			pstmt.setString(1, "sv-" + sensor + "-" + count);
			pstmt.setFloat(2, _sensor_value);
			pstmt.setTimestamp(3, _time);
			pstmt.setString(4, datas[0]);
			pstmt.setString(5, _serial);
			pstmt.setString(6, datas[1]);
			pstmt.setString(7, datas[2]);
			pstmt.setString(8, datas[3]);
			// sql문 적용
			pstmt.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		finally {
			try {
				if (con != null) { con.close(); }
				if (pstmt != null) { pstmt.close(); }
				if (rs != null) { rs.close(); }
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} // end finally
	} // end insertSensorValue
	
	// 설비를 제어할 센서값 가져오기 임예나
	public String equipmentSensorValueSelect(String _serial) {
		// sensor_type, content, crop 선언
		String[] sensor_type = {"temperature", "humidity", "co2"};
		String content = "";
		String crop = null;
		
		try {
			con = ds.getConnection();
			
			for (String st : sensor_type) {
				// 최근 센서값을 가져오는 sql문
				String query = "SELECT sv.sensor_value " + 
							   "FROM sensor_value AS sv " + 
							   "JOIN sensor AS s " + 
							   "ON sv.sensor_id = s.sensor_id " + 
							   "WHERE sv.robo_serial = ? AND s.sensor_type = ? " + 
							   "ORDER BY sv.measure_time DESC";
				pstmt = con.prepareStatement(query);
				// 매개변수 값 대입 -> set 메서드에 값 설정
				pstmt.setString(1, _serial);
				pstmt.setString(2, st);
				// sql문 실행
				rs = pstmt.executeQuery();
				
				if (rs.next()) {
					// content에 추가
					content += (int)rs.getFloat("sensor_value") + ", ";
				} else { // 센서 값이 없으면 0으로 채우기
					// content에 추가
					content += "0, ";
				}
			}
			content += " / ";
			// 품종 번호를 가져오는 query문
			String query = "SELECT crops_id " + 
						   "FROM robo " + 
						   "WHERE robo_serial = ? ";
			pstmt = con.prepareStatement(query);
			// 매개변수 값 대입 -> set 메서드에 값 설정
			pstmt.setString(1, _serial);
			// sql문 실행
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				// crop에 대입하기
				crop = rs.getString("crops_id");
			}
			
			// temperature, time 선언
			String[] temperature = new String[2];
			Timestamp time = new Timestamp(System.currentTimeMillis()); // 현재 시간 가져오기
			
			// time 변수에서 월, 시만 가져오기
			SimpleDateFormat sdf = new SimpleDateFormat("MM HH");
			String tmp = sdf.format(time);
			
			String[] mh = tmp.split(" "); // 월, 시 나누기
			String weather = null;
			
			// 월에 따라 계절 넣기
			switch (mh[0]) {
				case "05" :
				case "06" :
				case "07" :
				case "08" :
				case "09" :
					temperature[0] = "sum_";
					temperature[1] = "sum_";
					weather = "sum";
					break;
				case "01" :
				case "02" :
				case "03" :
				case "04" :
				case "10" :
				case "11" :
				case "12" :
					temperature[0] = "win_";
					temperature[1] = "win_";
					weather = "win";
			} // end switch
			
			// 시간에 따라 오전, 오후, 야간, 주간, 야간 설정하기
			if (weather.equals("sum")) { // 여름일 경우
				switch (mh[1]) {
					case "05" :
					case "06" :
					case "07" :
					case "08" :
					case "09" :
					case "10" :
					case "11" :
						temperature[0] += "mrn_";
						temperature[1] += "mrn_";
						break;
					case "12" :
					case "13" :
					case "14" :
					case "15" :
					case "16" :
					case "17" :
						temperature[0] += "aft_";
						temperature[1] += "aft_";
						break;
					case "18" :
					case "19" :
					case "20" :
					case "21" :
					case "22" :
					case "23" :
					case "24" :
					case "01" :
					case "02" :
					case "03" :
					case "04" :
						temperature[0] += "ngh_";
						temperature[1] += "ngh_";
				} // end switch
			} else { // 겨울일 경우
				switch (mh[1]) {
					case "06" :
					case "07" :
					case "08" :
					case "09" :
					case "10" :
					case "11" :
					case "12" :
					case "13" :
					case "14" :
					case "15" :
					case "16" :
					case "17" :
					case "18" :
						temperature[0] += "day_";
						temperature[1] += "day_";
						break;
					case "19" :
					case "20" :
					case "21" :
					case "22" :
					case "23" :
					case "00" :
					case "01" :
					case "02" :
					case "03" :
					case "04" :
					case "05" :
						temperature[0] += "ngh_";
						temperature[1] += "ngh_";
				} // end switch
			}
			
			temperature[0] += "min_temperature";
			temperature[1] += "max_temperature";
			
			// 비교 조건을 가져오는 sql문
			query = "SELECT " + temperature[0] + ", " + temperature[1] + ", " + "min_humidity, max_humidity, max_co2 " + 
					"FROM crops " + 
					"WHERE crops_id = ?";
			pstmt = con.prepareStatement(query);
			// 매개변수 값 대입 -> set 메서드에 값 설정
			pstmt.setString(1, crop);
			// sql문 실행
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				// content에 추가
				content = content + 
						  (int)rs.getFloat(temperature[0]) + ", " +
						  (int)rs.getFloat(temperature[1]) + ", " +
						  (int)rs.getFloat("min_humidity") + ", " +
						  (int)rs.getFloat("max_humidity") + ", " +
						  (int)rs.getFloat("max_co2");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (con != null) { con.close(); }
				if (pstmt != null) { pstmt.close(); }
				if (rs != null) { rs.close(); }
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} // end finally
		// 로보한테 보낼 메시지 return
		return content;
	} // end equipmentSensorValueSelect
	
	// 설비 상태를 DB에 저장하기 임예나
	public void equipmentStatusInsert (Timestamp _time, String _serial, String _equipment_name, boolean _control_status) {
		try {
			// gh_id, phone_number, equipment_id, count 선언
			String _gh_id = null;
			String _id = null;
			String _equipment_id = null;
			int count = 0;
			
			// 온실 id, 핸드폰 번호를 가져오는 sql문
			String query = "SELECT gh_id " + 
						   "FROM robo " + 
						   "WHERE robo_serial = ?";
			pstmt = con.prepareStatement(query);
			// 매개변수 값 대입 -> set 메서드에 값 설정
			pstmt.setString(1, _serial);
			// sql문 실행
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				// gh_id, phone_number 대입
				_gh_id = rs.getString("gh_id");
				_id = rs.getString("id");
			}
			
			// 설비 ID를 가져오는 sql문 
			query = "SELECT equipment_id " + 
					"FROM equipment " + 
					"WHERE gh_id = ? AND equipment_name = ?";
			pstmt = con.prepareStatement(query);
			// 매개변수 값 대입 -> set 메서드에 값 설정
			pstmt.setString(1, _gh_id);
			pstmt.setString(2, _equipment_name);
			// sql문 실행
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				// equipment_id 대입
				_equipment_id = rs.getString("equipment_id");
			}
			
			String[] gh = _gh_id.split("-");
			
			// sensor_value 갯수를 가져오는 sql문
			query = "SELECT COUNT(*) " + 
					"FROM equipment_control_log " + 
					"WHERE equipment_id = ?";
		
			pstmt = con.prepareStatement(query);
			// 매개변수 값 대입 -> set 메서드에 값 설정
			pstmt.setString(1, _equipment_id);
			// sql문 실행
			rs = pstmt.executeQuery();
			
			if (rs.next()) { // 행이 있으면 갯수 + 1 가져오기
				count = rs.getInt(1) + 1;
			} else { // 행이 없으면 1 지정하기
				count = 1;
			}
			
			query = "INSERT INTO equipment_control_log (control_log_id, control_status, control_time, equipment_id, gh_id, id) " + 
					"VALUES (?, ?, ?, ?, ?, ?)";
			pstmt = con.prepareStatement(query);
			// 매개변수 값 대입 -> set 메서드에 값 설정
			pstmt.setString(1, "ecl-" + _id + "-" + gh[2] + "-" + count);
			pstmt.setBoolean(2, _control_status);
			pstmt.setTimestamp(3, _time);
			pstmt.setString(4, _equipment_id);
			pstmt.setString(5, _gh_id);
			pstmt.setString(6, _id);
			// sql문 적용
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (con != null) { con.close(); }
				if (pstmt != null) { pstmt.close(); }
				if (rs != null) { rs.close(); }
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} // end finally
	} // end equipmentStatusInsert
	
	// 온실 목록 가져오기 임예나
	public ArrayList<HandyFarmDTO> GHSelect(String _id) {
		// list 선언
		ArrayList<HandyFarmDTO> list = new ArrayList<HandyFarmDTO>();
		
		try {
			// DB 연결
			con = ds.getConnection();
			
			// 온실 목록을 가져오는 sql문
			String query = "SELECT * " + 
						   "FROM greenhouse " + 
						   "WHERE id = ?";
			pstmt = con.prepareStatement(query);
			// 매개변수 값 대입 -> set 메서드에 값 설정
			pstmt.setString(1, _id);
			// sql문 실행
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				// 레코드의 정보를 각 변수에 저장
				String gh_id = rs.getString("gh_id");
				String gh_img = rs.getString("gh_img");
				String gh_nickname = rs.getString("gh_nickname");
				
				// data 객체 선언
				HandyFarmDTO data = new HandyFarmDTO();
				
				// data 객체의 set 메서드에 해당하는 값을 설정
				data.setGh_id(gh_id);
				data.setGh_nickname(gh_nickname);
				if (gh_img != null) { // DB에 저장된 온실 사진 가져오기
					data.setGh_img(gh_img);
				} else { // DB에 저장된 사진이 없으면 HandyFarm Logo 사진 가져오기
					data.setGh_img("../icon/handyfarm_logo.png");
				}
				
				list.add(data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (con != null) { con.close(); }
				if (pstmt != null) { pstmt.close(); }
				if (rs != null) { rs.close(); }
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} // end finally
		// 조회 결과 list 리턴
		return list;
	} // end GHSelect

	// 로보 목록 가져오기 임예나
	public ArrayList<HandyFarmDTO> RoboSelect(String _gh_id) {
		// list 선언
		ArrayList<HandyFarmDTO> list = new ArrayList<HandyFarmDTO>();
		
		try {
			// DB 연결
			con = ds.getConnection();
			
			// 로보 목록을 가져오는 sql문
			String query = "SELECT * " + 
						   "FROM robo AS r " + 
						   "JOIN crops AS c " + 
						   "ON r.crops_id = c.crops_id " + 
						   "WHERE gh_id = ?";
			pstmt = con.prepareStatement(query);
			// 매개변수 값 대입 -> set 메서드에 값 설정
			pstmt.setString(1, _gh_id);
			// sql문 실행
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				// 레코드의 정보를 각 변수에 저장
				String robo_serial = rs.getString("robo_serial");
				String robo_img = rs.getString("robo_img");
				String robo_nickname = rs.getString("robo_nickname");
				String crops_name = rs.getString("crops_name");
				
				// data 객체 선언
				HandyFarmDTO data = new HandyFarmDTO();
				
				// data 객체의 set 메서드에 해당하는 값을 설정
				data.setRobo_serial(robo_serial);
				data.setRobo_nickname(robo_nickname);
				data.setCrops_name(crops_name);
				if (robo_img != null) { // DB에 저장된 로보 사진 가져오기
					data.setRobo_img(robo_img);
				} else { // DB에 저장된 사진이 없으면 HandyFarm Logo 사진 가져오기
					data.setRobo_img("../icon/handyfarm_logo.png");
				}
				
				list.add(data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (con != null) { con.close(); }
				if (pstmt != null) { pstmt.close(); }
				if (rs != null) { rs.close(); }
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} // end finally
		// 조회 결과 list 리턴
		return list;
	} // end RoboSelect
	
	// 온실 이름 가져오기 임예나
	public String getGHNickname(String _gh_id) {
		String gh_name = null;
		try {
			// DB 연결
			con = ds.getConnection();
			
			// 온실 이름 조회
			String query = "SELECT gh_nickname " + 
						   "FROM greenhouse " + 
						   "WHERE gh_id = ?";
			
			pstmt = con.prepareStatement(query);
			// 매개변수 값 대입 -> set 메서드에 값 설정
			pstmt.setString(1,  _gh_id);
			// sql문 실행
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				gh_name = rs.getString("gh_nickname");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (con != null) { con.close(); }
				if (pstmt != null) { pstmt.close(); }
				if (rs != null) { rs.close(); }
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} // end finally
		// 조회 결과 gh_name 리턴
		return gh_name;
	} // end getGHName
	
	// 생장 정보 가져오기 임예나
	public  ArrayList<HandyFarmDTO> growth(String _gh_id, String _crops_id) {
		// sensor, list 선언
		ArrayList<String> sensor = new ArrayList<String>();
		ArrayList<HandyFarmDTO> list = new ArrayList<HandyFarmDTO>();
		
		try {
			// DB 연결
			con = ds.getConnection();
			
			// 해당 작물 in 온실에 있는 센서 종류 조회
			String query = "SELECT sensor_type " + 
						   "FROM sensor " + 
						   "WHERE gh_id = ? AND crops_id = ?";
			
			pstmt = con.prepareStatement(query);
			// 매개변수 값 대입 -> set 메서드에 값 설정
			pstmt.setString(1, _gh_id);
			pstmt.setString(2, _crops_id);
			// sql문 실행
			rs = pstmt.executeQuery();
			
			// 레코드 값만큼 반복
			while (rs.next()) {
				// 레코드의 정보를 변수에 저장
				String sensor_type = rs.getString("sensor_type");
				
				sensor.add(sensor_type);
			}
			
			for (String _sensor_type : sensor) {
				// 해당 작물 in 온실에 대한 생장환경 정보 조회 sql문
				query = "SELECT sv.sensor_value " + 
						"FROM sensor_value AS sv " + 
						"JOIN sensor AS s " + 
						"ON sv.sensor_id = s.sensor_id " + 
						"WHERE sv.gh_id = ? AND sv.crops_id = ? AND s.sensor_type = ? " +
						"ORDER BY sv.measure_time DESC";
				
				pstmt = con.prepareStatement(query);
				// 매개변수 값 대입 -> set 메서드에 값 설정
				pstmt.setString(1, _gh_id);
				pstmt.setString(2, _crops_id);
				pstmt.setString(3, _sensor_type);
				// sql문 실행
				rs = pstmt.executeQuery();
				
				if (rs.next()) {
					// 레코드의 정보를 각 변수에 저장
					float sensor_value = rs.getFloat("sensor_value");
					
					
					// data 객체 선언
					HandyFarmDTO data = new HandyFarmDTO();
					
					// data 객체의 set 메서드에 해당하는 값을 설정
					data.setSensor_type(_sensor_type);
					data.setSensor_value(sensor_value);
					
					if (_sensor_type.equals("temperature")) {
						data.setSensor_name("온도");
						data.setSensor_unit("℃");
					} else if (_sensor_type.equals("humidity")) {
						data.setSensor_name("습도");
						data.setSensor_unit("%");
					} else if (_sensor_type.equals("co2")) {
						data.setSensor_name("이산화탄소");
						data.setSensor_unit("단계");
					} else if (_sensor_type.equals("soil-moisture")) {
						data.setSensor_name("토양 수분도");
						data.setSensor_unit("%");
					} else if (_sensor_type.equals("sunshine")) {
						data.setSensor_name("일조량");
						data.setSensor_unit("lx");
					}
					
					list.add(data);
				} // end if
			} // end for
			
			// 해당 온실 in 작물에 대한 수확 가능 비율 정보 조회 sql문
			query = "SELECT harvestable " + 
					"FROM harvestable " + 
					"WHERE gh_id = ? AND crops_id = ? " + 
					"ORDER BY upload_time DESC";
			
			pstmt = con.prepareStatement(query);
			// 매개변수 값 대입 -> set 메서드에 값 설정
			pstmt.setString(1, _gh_id);
			pstmt.setString(2, _crops_id);
			// sql문 실행
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				// 레코드의 정보를 각 변수에 저장
				float harvestable = rs.getFloat("harvestable");
				
				// data 객체 선언
				HandyFarmDTO data = new HandyFarmDTO();
				
				// data 객체의 set 메서드에 해당하는 값을 설정
				data.setSensor_type("harvestable");
				data.setSensor_value(harvestable);
				data.setSensor_name("수확 가능 비율");
				data.setSensor_unit("%");
				
				list.add(data);
			} else {
				// data 객체 선언
				HandyFarmDTO data = new HandyFarmDTO();
				
				// data 객체의 set 메서드에 해당하는 값을 설정
				data.setSensor_type("harvestable");
				data.setSensor_value(0);
				data.setSensor_name("수확 가능 비율");
				data.setSensor_unit("%");
				
				list.add(data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (con != null) { con.close(); }
				if (pstmt != null) { pstmt.close(); }
				if (rs != null) { rs.close(); }
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} // end finally
		// 조회 결과 list 리턴
		return list;
	} // end growth
}