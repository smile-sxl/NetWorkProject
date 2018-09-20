package com.smile.networkproject;

/**
 * author: smile .
 * date: On 2018/8/28
 */
public class Student {

    /**
     * code : 101
     * message : SUCCESS
     * student : {"id":1,"name":"香茗","password":"123","phone":"18811616919","email":"123","sex":"1"}
     */

    private String code;
    private String message;
    private StudentBean student;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public StudentBean getStudent() {
        return student;
    }

    public void setStudent(StudentBean student) {
        this.student = student;
    }

    public static class StudentBean {
        /**
         * id : 1
         * name : 香茗
         * password : 123
         * phone : 18811616919
         * email : 123
         * sex : 1
         */

        private int id;
        private String name;
        private String password;
        private String phone;
        private String email;
        private String sex;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }
    }
}
