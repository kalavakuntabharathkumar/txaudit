package demo.secure;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.Map;

@RestController
public class TransactionController {
  private final TransactionService service; private final SecurityConfig security;
  public TransactionController(TransactionService s,SecurityConfig c){service=s;security=c;}

  @GetMapping("/actuator/health")
  public Map<String,String> health(){return Map.of("status","UP");}

  @GetMapping("/accounts/{id}")
  public Map<String,Object> account(@PathVariable long id){
    Account a=service.get(id); if(a==null) throw new IllegalArgumentException("Account not found");
    return Map.of("id",a.getId(),"balance",a.getBalance());
  }

  @PostMapping("/transactions")
  public Map<String,Object> transaction(@RequestBody Map<String,Object> body){
    long id=((Number)body.getOrDefault("account_id",1)).longValue();
    String type=(String)body.getOrDefault("type","DEPOSIT");
    BigDecimal amount=new BigDecimal(body.get("amount").toString());
    Account a=service.apply(id,type,amount);
    return Map.of("status","COMPLETED","account_id",id,"balance",a.getBalance());
  }

  @GetMapping("/audit/health")
  public Map<String,String> audit(){return Map.of("audit","available");}

  @GetMapping("/auth/token")
  public Map<String,String> token(@RequestParam(defaultValue="admin") String user,
                                  @RequestParam(defaultValue="ADMIN") String role){
    return Map.of("token",security.demoToken(user,role));
  }
}
