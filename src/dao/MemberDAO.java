package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Member;
import util.DatabaseConnection;

public class MemberDAO {

    // ADD A NEW MEMBER
    public boolean addMember(Member member) {
        String sql = "INSERT INTO members (name, email, phone, membership_date, member_type) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, member.getName());
            stmt.setString(2, member.getEmail());
            stmt.setString(3, member.getPhone());
            stmt.setString(4, member.getMembershipDate());
            stmt.setString(5, member.getMemberType());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            System.out.println("Error adding member: " + e.getMessage());
            return false;
        }
    }

    // GET ALL MEMBERS
    public List<Member> getAllMembers() {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT * FROM members";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Member member = new Member(
                        rs.getInt("member_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("membership_date"),
                        rs.getString("member_type")
                );
                members.add(member);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching members: " + e.getMessage());
        }
        return members;
    }

    // GET MEMBER BY ID
    public Member getMemberById(int memberId) {
        String sql = "SELECT * FROM members WHERE member_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, memberId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Member(
                        rs.getInt("member_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("membership_date"),
                        rs.getString("member_type")
                );
            }

        } catch (SQLException e) {
            System.out.println("Error fetching member: " + e.getMessage());
        }
        return null;
    }
}