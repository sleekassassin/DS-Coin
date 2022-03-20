package DSCoinPackage;

import HelperClasses.CRF;

public class BlockChain_Honest {

  public int tr_count;
  public static final String start_string = "DSCoin";
  public TransactionBlock lastBlock;

  public void InsertBlock_Honest (TransactionBlock newBlock) {
	CRF unique=new CRF(64);
	if(this.lastBlock==null){
		this.tr_count=newBlock.tr_count;
		this.lastBlock=newBlock;
		//String nonce;
		long t=1000000000;
		long lst=(long)(1e10);
		while(t<lst){
			if(unique.Fn(start_string+"#"+newBlock.trsummary+"#"+Long.toString(t)).substring(0,4).equals("0000")){
				newBlock.nonce=Long.toString(t);
				break;
			}
			t++;
		}
		newBlock.dgst=unique.Fn(start_string+"#"+newBlock.trsummary+"#"+newBlock.nonce);
	}
	else{
		newBlock.previous=this.lastBlock;
		this.lastBlock=newBlock;
		long t=1000000000;
		long lst=(long)(1e10);
		while(t<lst){
			if(unique.Fn(newBlock.previous.dgst+"#"+newBlock.trsummary+"#"+Long.toString(t)).substring(0,4).equals("0000")){
				newBlock.nonce=Long.toString(t);
				break;
			}
			t++;
		}
		newBlock.dgst=unique.Fn(newBlock.previous.dgst+"#"+newBlock.trsummary+"#"+newBlock.nonce);
	}
  }
}