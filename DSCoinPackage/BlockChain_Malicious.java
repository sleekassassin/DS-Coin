package DSCoinPackage;

import HelperClasses.CRF;
import HelperClasses.MerkleTree;
public class BlockChain_Malicious {

	public int tr_count;
	public static final String start_string = "DSCoin";
	public TransactionBlock[] lastBlocksList;
	/*
	public static boolean checkMerkleTree (TreeNode t,int len,int ind,Transaction[] arr){
		CRF unique=new CRF(64);
		if(t.left==null){
			String s0;
			if(arr[ind-len+1].coinsrc_block==null){
				s0="Genesis";
			}
			else{
				s0=arr[ind-len+1].coinsrc_block.dgst;
			}
			if(t.val!=unique.Fn(arr[ind-len+1].coinID+arr[ind-len+1].Source.UID+arr[ind-len+1].Destination.UID+s0)){
				return false;
			}
			else{
				return true;
			}
		}
		else{
			if(checkMerkleTree(t.left,len,2*ind+1,arr) && checkMerkleTree(t.right,len,2*ind+2,arr)){
				if(t.val!=unique.Fn(t.left.val+"#"+t.right.val)){
					return false;
				}
				else{
					return true;
				}
			}
			else{
				return false;
			}
		}
	}
	*/
	public static boolean checkallTransaction(TransactionBlock tB){
		boolean b=true;
		if(tB.previous!=null){
			for(int i=0;i<tB.tr_count;i++){
				b&=tB.previous.checkTransaction(tB.trarray[i]);
			}
		}
		return b;
	}
	public static boolean checkTransactionBlock (TransactionBlock tB) {
		CRF unique=new CRF(64);
		MerkleTree mt=new MerkleTree();
		mt.Build(tB.trarray);
		String s;
		if(tB.previous==null){
			s="DSCoin";
		}
		else{
			s=tB.previous.dgst;
		}
		if(!tB.dgst.substring(0,4).equals("0000")){
			return false;
		}
		else if(!tB.dgst.equals(unique.Fn(s+"#"+ tB.trsummary+"#"+tB.nonce))){
			return false;
		}
		else if(!tB.trsummary.equals(tB.Tree.rootnode.val)){
			return false;
		}
		else if(!(mt.matchTree(mt.rootnode,tB.Tree.rootnode))){
			return false;
		}
		else if(!checkallTransaction(tB)){
			return false;
		}
		else{
			return true;
		}
	}

	public TransactionBlock FindLongestValidChain () {
		int tot=0;
		while(this.lastBlocksList[tot]!=null){
			tot++;
		}
		if(tot==0){
			return null;
		}
		else{
			TransactionBlock output=this.lastBlocksList[0];
			int mx=0;
			int ind=0;
			while(ind<tot){
				int len=0;
				TransactionBlock curr=this.lastBlocksList[ind];
				TransactionBlock ans=this.lastBlocksList[ind];
				while(curr!=null){
					if(len==0){
						if(checkTransactionBlock(curr)){
							ans=curr;
							len+=1;
						}
						curr=curr.previous;
					}
					else{
						if(checkTransactionBlock(curr)){
							len+=1;
						}
						else{
							len=0;
						}
						curr=curr.previous;
					}
				}
				if(len>mx){
					output=ans;
					mx=len;
				}
				ind+=1;
			}
			return output;
		}
	}

	public void InsertBlock_Malicious (TransactionBlock newBlock) {
		CRF unique=new CRF(64);
		TransactionBlock last=this.FindLongestValidChain();
		newBlock.previous=last;
		
		if(last==null){
			this.tr_count=newBlock.tr_count;
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
		int ind=-1;
		for(int i=0;i<100;i++){
			ind=i;
			if(this.lastBlocksList[i]==null){
				break;
			}
			else if(last==this.lastBlocksList[i]){
				break;
			}
			
		}
		this.lastBlocksList[ind]=newBlock;
	}
}
