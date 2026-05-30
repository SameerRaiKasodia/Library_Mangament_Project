package model;

public class Member {

    private int memberId;
    private String name;
    private String email;
    private String phone;
    private String membershipDate;
    private String memberType;   // "STUDENT" or "FACULTY"

    // Default constructor
    public Member() {
    }

    // Parameterized constructor
    public Member(int memberId, String name, String email, String phone,
                  String membershipDate, String memberType) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.membershipDate = membershipDate;
        this.memberType = memberType;
    }

    // Getters and Setters
    public int getMemberId() { return memberId; }
    public void setMemberId(int memberId) { this.memberId = memberId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getMembershipDate() { return membershipDate; }
    public void setMembershipDate(String membershipDate) { this.membershipDate = membershipDate; }

    public String getMemberType() { return memberType; }
    public void setMemberType(String memberType) { this.memberType = memberType; }

    @Override
    public String toString() {
        return "Member [ID=" + memberId + ", Name=" + name + ", Email=" + email +
                ", Phone=" + phone + ", Date=" + membershipDate +
                ", Type=" + memberType + "]";
    }
}