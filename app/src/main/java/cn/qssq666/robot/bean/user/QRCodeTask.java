package cn.qssq666.robot.bean.user;

public class QRCodeTask {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    int id;
    String qrcode;

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    String order;

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    double money;

    public int getTasktype() {
        return tasktype;
    }

    public void setTasktype(int tasktype) {
        this.tasktype = tasktype;
    }

    @Override
    public String toString() {
        return "QRCodeTask{" +
                "id=" + id +
                ", qrcode='" + qrcode + '\'' +
                ", order='" + order + '\'' +
                ", money=" + money +
                '}';
    }

    int tasktype;

    public int getPaytype() {
        return paytype;
    }

    public void setPaytype(int paytype) {
        this.paytype = paytype;
    }

    int paytype;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    int status;
    public static int TYPE_GENEREATE_QRCODE=0;
    public static int TYPE_PAY_RESULT_QURY=1;
}
