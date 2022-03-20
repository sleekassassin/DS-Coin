package DSCoinPackage;

import java.util.*;
import HelperClasses.Pair;
import HelperClasses.TreeNode;
import HelperClasses.MerkleTree;

public class Members
 {

	public String UID;
	public List<Pair<String, TransactionBlock>> mycoins;
	public Transaction[] in_process_trans;

	public void initiateCoinsend(String destUID, DSCoin_Honest DSobj) {
		//int len=this.mycoins.length();
		Pair<String, TransactionBlock> spend=this.mycoins.get(0);
		int ind=0;
		/*for(int i=0;i<len;i++){
			if(this.mycoins.get(i).First.compareTo(spend.First)<0){
				spend=this.mycoins.get(i);
				ind=i;
			}
		}*/
		this.mycoins.remove(ind);
		Transaction latest=new Transaction();
		
		//member dhundhte hain ab
		int tmem=DSobj.memberlist.length;
		Members earner=new Members();
		for(int i=0;i<tmem;i++){
			if(DSobj.memberlist[i].UID.equals(destUID)){
				earner=DSobj.memberlist[i];
				break;
				//agar na mile to
			}
		}
		
		latest.coinID=spend.first;
		latest.Source=this;
		latest.Destination=earner;
		latest.coinsrc_block=spend.second;
		
		//pendingTransactions transaction queue addition
		DSobj.pendingTransactions.AddTransactions(latest);
		
		// in process array updation
		int j=0;
		while(this.in_process_trans[j]!=null){
			j++;
		}
		this.in_process_trans[j]=latest;
		/*
		int tpendTrans=this.in_process_trans.length;
		Transaction[] update=new Transaction[tpendTrans+1];
		for(int i=0;i<tpendTrans;i++){
			update[i]=this.in_process_trans[i];
		}
		update[tpendTrans]=latest;
		this.in_process_trans=update;
		*/
	}
	
	public void initiateCoinsend(String destUID, DSCoin_Malicious DSobj) {
		//int len=this.mycoins.length();
		Pair<String, TransactionBlock> spend=this.mycoins.get(0);
		int ind=0;
		/*for(int i=0;i<len;i++){
			if(this.mycoins.get(i).First.compareTo(spend.First)<0){
				spend=this.mycoins.get(i);
				ind=i;
			}
		}*/
		this.mycoins.remove(ind);
		Transaction latest=new Transaction();
		
		//member dhundhte hain ab
		int tmem=DSobj.memberlist.length;
		Members earner=new Members();
		for(int i=0;i<tmem;i++){
			if(DSobj.memberlist[i].UID.equals(destUID)){
				earner=DSobj.memberlist[i];
				break;
				//agar na mile to
			}
		}
		
		latest.coinID=spend.first;
		latest.Source=this;
		latest.Destination=earner;
		latest.coinsrc_block=spend.second;
		
		//pendingTransactions transaction queue addition
		DSobj.pendingTransactions.AddTransactions(latest);
		
		// in process array updation
		int j=0;
		while(this.in_process_trans[j]!=null){
			j++;
		}
		this.in_process_trans[j]=latest;
		/*
		int tpendTrans=this.in_process_trans.length;
		Transaction[] update=new Transaction[tpendTrans+1];
		for(int i=0;i<tpendTrans;i++){
			update[i]=this.in_process_trans[i];
		}
		update[tpendTrans]=latest;
		this.in_process_trans=update;
		*/
	}
	
	
	public Pair<List<Pair<String, String>>, List<Pair<String, String>>> finalizeCoinsend (Transaction tobj, DSCoin_Honest DSObj) throws MissingTransactionException {
		TransactionBlock tB=DSObj.bChain.lastBlock;
		int f=-1;
		while(tB!=null){
			int tot=tB.tr_count;
			for(int i=0;i<tot;i++){
				if(tobj==tB.trarray[i]){
					f=i;
					break;
				}
			}
			if(f!=-1){
				break;
			}
			tB=tB.previous;
		}
		if(f==-1){
			throw new MissingTransactionException();
		}
		else{
			//sibling coupled path to root
			List<Pair<String,String>> path=new ArrayList<Pair<String,String>>();
			Pair<String,String> first=new Pair<String,String>(tB.Tree.rootnode.val,null);
			path.add(first);
			TreeNode current=tB.Tree.rootnode;
			int tot=tB.tr_count;
			while(tot>1){
				Pair<String,String> p=new Pair<String,String>(current.left.val,current.right.val);
				path.add(p);
				if(f<tot/2){
					current=current.left;
					tot/=2;
				}
				else{
					current=current.right;
					f-=tot/2;
					tot/=2;
				}
			}
			int len=path.size();
			
			List<Pair<String,String>> list1=new ArrayList<Pair<String,String>>();
			while(len-->0){
				Pair<String,String> p=path.get(len);
				list1.add(p);
			}
			
			//the k+2 pairs list(list2)
			List<Pair<String,String>> list2=new ArrayList<Pair<String,String>>();
			if(tB.previous==null){
				Pair<String,String> pahla=new Pair<String,String>("DSCoin",null);
				list2.add(pahla);
			}
			else{
				Pair<String,String> pahla=new Pair<String,String>(tB.previous.dgst,null);
				list2.add(pahla);
			}
			List<Pair<String,String>> ulta=new ArrayList<Pair<String,String>>();
			TransactionBlock again=DSObj.bChain.lastBlock;
			int k=0;
			while(again!=tB){
				Pair<String,String> p=new Pair<String,String>(again.dgst,again.previous.dgst+"#"+again.trsummary+"#"+again.nonce);
				ulta.add(p);
				k+=1;
				again=again.previous;
			}
			if(again.previous==null){
				Pair<String,String> akhiri=new Pair<String,String>(again.dgst,"DSCoin"+"#"+again.trsummary+"#"+again.nonce);
				list2.add(akhiri);
			}
			else{
				Pair<String,String> akhiri=new Pair<String,String>(again.dgst,again.previous.dgst+"#"+again.trsummary+"#"+again.nonce);
				list2.add(akhiri);
			}
			while(k-->0){
				Pair<String,String> p=ulta.get(k);
				list2.add(p);
			}
			int req=-1;
			for(int i=0;i<100;i++){
				if(this.in_process_trans[i]==tobj){
					req=i+1;
					break;
				}
			}
			//System.out.println(req);
			while(req<100 && this.in_process_trans[req]!=null){
				this.in_process_trans[req-1]=this.in_process_trans[req];
				req++;
			}
			this.in_process_trans[req-1]=null;
			Pair<String, TransactionBlock> gain=new Pair<String, TransactionBlock>(tobj.coinID,tB);
			int sz=tobj.Destination.mycoins.size();
			//System.out.println(tobj.Destination.UID + "&&&");
			if(sz==0 || gain.first.compareTo(tobj.Destination.mycoins.get(sz-1).first)>0){
				tobj.Destination.mycoins.add(gain);
			}
			else{
				int flag=-1;
				for(int i=0;i<sz;i++){
					if(gain.first.compareTo(tobj.Destination.mycoins.get(i).first)<0){
						flag=i;
						break;
					}
				}
				tobj.Destination.mycoins.add(flag,gain);
			}
			/*
			int siz=this.in_process_trans.length;
			Transaction [] update=new Transaction[siz-1];
			int w=0;
			for(int i=0;i<siz;i++){
				if(this.in_process_trans[i]!=tobj){
					update[w]=this.in_process_trans[i];
					w++;
				}
			}
			this.in_process_trans=update;
			*/
			Pair<List<Pair<String, String>>, List<Pair<String, String>>> ans=new Pair<List<Pair<String, String>>, List<Pair<String, String>>>(list1,list2);
			return ans;
			
		}
		//return null;
	}

	public void MineCoin(DSCoin_Honest DSObj) {
		int count=0;
		Transaction[] tarr=new Transaction[DSObj.bChain.tr_count];
		int ind=0;
		try{
			while(count!=DSObj.bChain.tr_count-1){
				Transaction t=DSObj.pendingTransactions.RemoveTransaction();
				TransactionBlock ck=DSObj.bChain.lastBlock;
				if(ck.checkTransaction(t)){
					boolean fl=true;
					for(int i=0;i<ind;i++){
						if(t.coinID.equals(tarr[i].coinID)){
							//just remove it
							fl=false;
							break;
						}
					}
					if(fl){
						tarr[ind]=t;
						ind+=1;
						count+=1;
					}
				}
				else{
					//just chill
				}
			}
		}
		catch(Exception e){
			System.out.println("Transaction Queue Empty!");
		}
		//reward is gain of 1 coin by miner
		Transaction reward=new Transaction();
		
		reward.coinID=Long.toString(Long.parseLong(DSObj.latestCoinID)+1);
		DSObj.latestCoinID=reward.coinID;
		reward.Destination=this;
		
		tarr[ind]=reward;
		ind+=1;
		count+=1;
		
		TransactionBlock tB=new TransactionBlock(tarr);
		DSObj.bChain.InsertBlock_Honest(tB);
		
		// adding coin to miner's wallet
		Pair<String, TransactionBlock> gain=new Pair<String, TransactionBlock>(reward.coinID,tB);
		int sz=this.mycoins.size();
		//this.mycoins.add(gain);
		if(sz==0 || gain.first.compareTo(this.mycoins.get(sz-1).first)>0){
			this.mycoins.add(gain);
		}
		else{
			int flag=-1;
			for(int i=0;i<sz;i++){
				if(gain.first.compareTo(this.mycoins.get(i).first)<0){
					flag=i;
					break;
				}
			}
			this.mycoins.add(flag,gain);
		}
		
	}  

	public void MineCoin(DSCoin_Malicious DSObj) {
		int count=0;
		Transaction[] tarr=new Transaction[DSObj.bChain.tr_count];
		TransactionBlock ck=DSObj.bChain.FindLongestValidChain();
		//System.out.println(ck.dgst);
		//System.out.println(ck.dgst+" &&&&");
		//System.out.println(checkTransactionBlock(ck));
		int ind=0;
		try{
			while(count!=DSObj.bChain.tr_count-1){
				Transaction t=DSObj.pendingTransactions.RemoveTransaction();
				if(ck.checkTransaction(t)){
					boolean fl=true;
					for(int i=0;i<ind;i++){
						if(t.coinID.equals(tarr[i].coinID)){
							//just remove it
							fl=false;
							break;
						}
					}
					if(fl){
						tarr[ind]=t;
						ind+=1;
						count+=1;
					}
				}
				else{
					//just chill
					//System.out.println("checkTransactionfails");
				}
			}
		}
		catch(Exception e){
			System.out.println("Transaction Queue Empty!");
		}
		//reward is gain of 1 coin by miner
		Transaction reward=new Transaction();
		
		reward.coinID=Long.toString(Long.parseLong(DSObj.latestCoinID)+1);
		DSObj.latestCoinID=reward.coinID;
		reward.Destination=this;
		
		tarr[ind]=reward;
		ind+=1;
		count+=1;
		/* for(int i=0;i<DSObj.bChain.tr_count;i++){
			if(tarr[i]==null){
				System.out.println("yahin gadbad hai" + " " + i);
			}
		} */
		TransactionBlock tB=new TransactionBlock(tarr);
		DSObj.bChain.InsertBlock_Malicious(tB);
		
		// adding coin to miner's wallet
		Pair<String, TransactionBlock> gain=new Pair<String, TransactionBlock>(reward.coinID,tB);
		int sz=this.mycoins.size();
		//this.mycoins.add(gain);
		if(sz==0 || gain.first.compareTo(this.mycoins.get(sz-1).first)>0){
			this.mycoins.add(gain);
		}
		else{
			int flag=-1;
			for(int i=0;i<sz;i++){
				if(gain.first.compareTo(this.mycoins.get(i).first)<0){
					flag=i;
					break;
				}
			}
			this.mycoins.add(flag,gain);
		}
	}  
}
