package demo.secure;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class TransactionService {
  private final ConcurrentHashMap<Long,Account> accounts = new ConcurrentHashMap<>();
  private final ConcurrentHashMap<Long,ReentrantLock> locks = new ConcurrentHashMap<>();
  public TransactionService(){ accounts.put(1L,new Account(1,new BigDecimal("1000.00"))); }

  public Account get(long id){ return accounts.get(id); }

  public Account apply(long id, String type, BigDecimal amount){
    if(amount.signum()<=0) throw new IllegalArgumentException("Amount must be positive");
    Account a=accounts.get(id); if(a==null) throw new IllegalArgumentException("Account not found");
    ReentrantLock lock=locks.computeIfAbsent(id,k->new ReentrantLock());
    lock.lock();
    BigDecimal old=a.getBalance();
    try{
      BigDecimal next = "DEPOSIT".equalsIgnoreCase(type) ? old.add(amount) : old.subtract(amount);
      if(next.signum()<0) throw new IllegalStateException("Insufficient funds");
      a.setBalance(next);
      return a;
    } catch(RuntimeException ex){
      a.setBalance(old); // rollback
      throw ex;
    } finally { lock.unlock(); }
  }
}
