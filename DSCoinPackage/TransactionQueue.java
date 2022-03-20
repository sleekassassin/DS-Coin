package DSCoinPackage;

public class TransactionQueue {

  public Transaction firstTransaction;
  public Transaction lastTransaction;
  public int numTransactions;

  public void AddTransactions (Transaction transaction) {
	if(this.numTransactions==0){
		this.firstTransaction=transaction;
		this.lastTransaction=transaction;
	}
	else{
		transaction.prev=this.lastTransaction;
		this.lastTransaction.next=transaction;
		this.lastTransaction=transaction;
	}
	this.numTransactions+=1;
  }
  
  public Transaction RemoveTransaction () throws EmptyQueueException {
	if(this.numTransactions==0){
		throw new EmptyQueueException();
		// throw gives the warning
	}
	else{
		Transaction output=new Transaction();
		output=this.firstTransaction;
		if(this.numTransactions==1){
			this.firstTransaction=null;
			this.lastTransaction=null;
		}
		else{
			this.firstTransaction=this.firstTransaction.next;
			this.firstTransaction.prev=null;
		}
		this.numTransactions-=1;
		return output;
	}
    //return null;
  }

  public int size() {
    return this.numTransactions;
  }
}
