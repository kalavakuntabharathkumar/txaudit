package demo.secure;
import org.junit.jupiter.api.*;
import java.math.BigDecimal;
import java.util.concurrent.*;
import static org.junit.jupiter.api.Assertions.*;

class TransactionServiceTest {
  @Test void depositAndWithdraw() {
    TransactionService s=new TransactionService();
    s.apply(1,"DEPOSIT",new BigDecimal("250"));
    assertEquals(new BigDecimal("1250.00"),s.get(1).getBalance());
    s.apply(1,"WITHDRAW",new BigDecimal("100"));
    assertEquals(new BigDecimal("1150.00"),s.get(1).getBalance());
  }
  @Test void insufficientFundsRollsBack() {
    TransactionService s=new TransactionService();
    assertThrows(IllegalStateException.class,()->s.apply(1,"WITHDRAW",new BigDecimal("2000")));
    assertEquals(new BigDecimal("1000.00"),s.get(1).getBalance());
  }
  @Test void concurrentWithdrawalsRemainConsistent() throws Exception {
    TransactionService s=new TransactionService(); ExecutorService pool=Executors.newFixedThreadPool(8);
    var tasks=java.util.stream.IntStream.range(0,20).mapToObj(i->(Callable<Boolean>)()->{
      try{s.apply(1,"WITHDRAW",new BigDecimal("10")); return true;}catch(Exception e){return false;}
    }).toList();
    for(Future<Boolean> f:pool.invokeAll(tasks)) f.get();
    pool.shutdown();
    assertEquals(new BigDecimal("800.00"),s.get(1).getBalance());
  }
}
