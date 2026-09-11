package demo.secure;
import java.math.BigDecimal;
public class Account {
  private final long id;
  private BigDecimal balance;
  public Account(long id, BigDecimal balance){this.id=id;this.balance=balance;}
  public long getId(){return id;}
  public BigDecimal getBalance(){return balance;}
  public void setBalance(BigDecimal b){balance=b;}
}
