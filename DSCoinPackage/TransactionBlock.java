package DSCoinPackage;

import HelperClasses.MerkleTree;
import HelperClasses.CRF;

public class TransactionBlock {

  public Transaction[] trarray;
  public TransactionBlock previous;
  public MerkleTree Tree;
  public String trsummary;
  public String nonce;
  public String dgst;
  public int tr_count;

  
  TransactionBlock(Transaction[] t) {
	this.tr_count=t.length;
    Transaction[] a=new Transaction[this.tr_count];//new memory allocation
	for(int i=0;i<this.tr_count;i++){
		a[i]=t[i];
	}
	this.trarray=a;
	this.previous=null;
	MerkleTree mtb=new MerkleTree();
	this.trsummary=mtb.Build(t);
	this.Tree=mtb;
	this.dgst=null;
  }

	public boolean checkTransaction (Transaction t) {
		if(t.coinsrc_block==null){
			//earned by miner
			return true;
		}
		else{
			boolean b1=false;//check for earning
			
			
			Transaction [] find=t.coinsrc_block.trarray;
			int len=find.length;
			//check for earning
			for(int i=0;i<len;i++){
				if(find[i].coinID.equals(t.coinID)){
					if(find[i].Destination.UID.equals(t.Source.UID)){
						b1=true;
						break;
					}
				}
			}
			if(b1){
				//check for double spending
				boolean b3=true;
				TransactionBlock tB=this;
				while(tB!=t.coinsrc_block){
					if(tB==null){
						break;
					}
					else{
						int m=tB.tr_count;
						for(int i=0;i<m;i++){
							if(t.coinID.equals(tB.trarray[i].coinID)){
								//System.out.println("double spend kar diya");
								b3=false;
								break;
							}
						}
						if(b3){
							tB=tB.previous;
						}
						else{
							break;
						}
					}
				}
				return b3;
			}
			else{
				//System.out.println("earn hi nahi kiya");
				return false;
			}
		}
	}
}
