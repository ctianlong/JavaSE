package test;

import java.io.Serializable;

/**
 * @author ctl
 * @createTime 2023/11/30 14:23
 * @description
 */
public class BytedanceAPIResult implements Serializable {

    private int err_no;

    private String err_tips;

    private String err_msg;

    public int getErr_no() {
        return err_no;
    }

    public void setErr_no(int err_no) {
        this.err_no = err_no;
    }

    public String getErr_tips() {
        return err_tips;
    }

    public void setErr_tips(String err_tips) {
        this.err_tips = err_tips;
    }

    public String getErr_msg() {
        return err_msg;
    }

    public void setErr_msg(String err_msg) {
        this.err_msg = err_msg;
    }

}
