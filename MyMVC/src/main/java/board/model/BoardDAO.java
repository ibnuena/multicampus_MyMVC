package board.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class BoardDAO {

	private DataSource ds;
	private Connection con;
	private PreparedStatement ps;
	private ResultSet rs;

	public BoardDAO() {
		try {
			Context ctx = new InitialContext();
			ds = (DataSource) ctx.lookup("java:comp/env/jdbc/myoracle");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// String => 불변성(immutable) 원본은 편집 불가. 새로운 객체를 만들어서 문자열을 추가 또는 삭제
	// StringBuffer/StringBuilder -> 문자열 편집가능
	public int insertBoard(BoardVO vo) throws SQLException {
		try {
			con = ds.getConnection();
			StringBuffer buf = new StringBuffer("insert into mvc_board(")
					.append(" num, name, passwd, title, content, fileName, fileSize, readnum, wdate)")
					.append(" values(mvc_board_seq.nextval, ?, ?, ?, ?, ?, ?, 0, sysdate)");
			String sql = buf.toString();
			ps = con.prepareStatement(sql);
			ps.setString(1, vo.getName());
			ps.setString(2, vo.getPasswd());
			ps.setString(3, vo.getTitle());
			ps.setString(4, vo.getContent());
			ps.setString(5, vo.getFileName());
			ps.setLong(6, vo.getFileSize());

			return ps.executeUpdate();
		} finally {
			close();
		}
	} // insertBoard ----------------------------------------------------

	/** 총 게시글 수 */
	public int getTotalCount() throws SQLException {
		try {
			con = ds.getConnection();
			String sql = "select count(num) cnt from mvc_board";
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			// 단일행 반환
			if (rs.next()) {
				int count = rs.getInt("cnt");
				return count;
			}
			return 0;
		} finally {
			close();
		}
	}// ----------------------------------------------

	/** 게시글 목록 */
	public List<BoardVO> listBoard(int start, int end) throws Exception {
		try {
			con = ds.getConnection();
//			String sql = "select * from mvc_board order by num desc"; // paging 처리 안할 때

			StringBuffer buf = new StringBuffer(" select * from(").append(" select rownum rn, a.* from")
					.append(" (select * from mvc_board order by num desc) a)").append(" where rn between ? and ?");

			String sql = buf.toString();
			ps = con.prepareStatement(sql);
			ps.setInt(1, start);
			ps.setInt(2, end);

			rs = ps.executeQuery();
			return makeList(rs);
		} finally {
			close();
		}
	} // listBoard ---------------------------------------

	public List<BoardVO> makeList(ResultSet rs) throws SQLException {
		List<BoardVO> arr = new ArrayList<>();

		while (rs.next()) {
			int num = rs.getInt("num");
			String name = rs.getString("name");
			String passwd = rs.getString("passwd");
			String title = rs.getString("title");
			String content = rs.getString("content");
			java.sql.Date wdate = rs.getDate("wdate");
			int readnum = rs.getInt("readnum");
			String fileName = rs.getString("fileName");
			long fileSize = rs.getLong("fileSize");
			BoardVO record = new BoardVO(num, name, passwd, title, content, wdate, readnum, fileName, fileSize);
			arr.add(record);
		} // while---------------------------

		return arr;
	} // makeList --------------------------------------------

	/** 글 번호로 글 내용 보기 */
	public BoardVO getBoard(int num) throws SQLException {
		try {
			con = ds.getConnection();
			String sql = "select * from mvc_board where num=?";
			ps = con.prepareStatement(sql);
			ps.setInt(1, num);

			rs = ps.executeQuery();
			List<BoardVO> arr = makeList(rs);
			if (arr == null || arr.size() == 0) {
				return null;
			}

			BoardVO vo = arr.get(0);
			return vo;

		} finally {
			close();
		}

	}

	/* 조회수 증가 **/
	public void updateReadnum(int num) throws SQLException {
		try {
			con = ds.getConnection();
			StringBuffer buf = new StringBuffer("update mvc_board set").append(" readnum=readnum+1 where num=?");
			String sql = buf.toString();
			ps = con.prepareStatement(sql);
			ps.setInt(1, num);
			ps.executeUpdate();
		} finally {
			close();
		}

	}

	/* 게시글 삭제 **/
	public int deleteBoard(int num) throws SQLException {
		try {
			con = ds.getConnection();
			String sql = "delete from mvc_board where num=?";
			ps = con.prepareStatement(sql);
			ps.setInt(1, num);
			return ps.executeUpdate();
		} finally {
			close();
		}
	}// ---------------------------------

	public int updateBoard(BoardVO vo) throws SQLException {
		try {
			con = ds.getConnection();
			StringBuffer buf = new StringBuffer("update mvc_board set name=?, passwd=?,")
					.append(" title=?, content=? ");
			if (vo.getFileName() != null) { // 첨부파일이 있다면
				buf.append(" , fileName=?, fileSize=?");
			}
			buf.append(" where num=?");
			String sql = buf.toString();
			System.out.println(sql);
			ps = con.prepareStatement(sql);
			ps.setString(1, vo.getName());
			ps.setString(2, vo.getPasswd());
			ps.setString(3, vo.getTitle());
			ps.setString(4, vo.getContent());
			if (vo.getFileName() != null) {
				ps.setString(5, vo.getFileName());
				ps.setLong(6, vo.getFileSize());
				ps.setInt(7, vo.getNum());
			} else {
				ps.setInt(5, vo.getNum());
			}

			return ps.executeUpdate();
		} finally {
			close();
		}
	}

	/* 글 검색 결과 게시글 수 **/
	public int getFindTotalCount(String findType, String findKeyword) throws SQLException {
		
		try {
			con = ds.getConnection();
			String sql = "select count(num) cnt from mvc_board where " + findType + " like ? ";
			ps = con.prepareStatement(sql);
			
			ps.setString(1, "%" + findKeyword + "%");
			
			rs = ps.executeQuery();

			// 단일행 반환
			if (rs.next()) {
				int count = rs.getInt("cnt");
				return count;
			}
			return 0;
		} finally {
			close();
		}
	} // ---------------------------------------

	public List<BoardVO> findBoard(int start, int end, String findType, String findKeyword) throws SQLException {
		try {
			con = ds.getConnection();

			StringBuffer buf = new StringBuffer(" select * from(").append(" select rownum rn, a.* from")
					.append(" (select * from mvc_board where " + findType + "  like ? order by num desc) a)")
					.append(" where rn between ? and ?");

			String sql = buf.toString();
			ps = con.prepareStatement(sql);
			ps.setString(1, "%" + findKeyword + "%");
			ps.setInt(2, start);
			ps.setInt(3, end);
			
			rs = ps.executeQuery();
			return makeList(rs);
		} finally {
			close();
		}		
	}
	
	public void close() {
		try {
			if (rs != null)
				rs.close();
			if (ps != null)
				ps.close();
			if (con != null)
				con.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}


}
