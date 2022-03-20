package DSCoinPackage;
import HelperClasses.Pair;

public class Moderator
	{
	
	public void initializeDSCoin(DSCoin_Honest DSObj, int coinCount) {
		Members leader=new Members();
		leader.UID="Moderator";
		int tot_mem=DSObj.memberlist.length;
		int ind=0;
		int coin=100000;
		int no_tB=coinCount/DSObj.bChain.tr_count;
		for(int i=0;i<no_tB;i++){
			Transaction[] a=new Transaction[DSObj.bChain.tr_count];
			for(int j=0;j<DSObj.bChain.tr_count;j++){
				Transaction t=new Transaction();
				t.coinID=Integer.toString(coin);
				coin+=1;
				t.Source=leader;
				t.Destination=DSObj.memberlist[ind%tot_mem];
				ind+=1;
				a[j]=t;
			}
			TransactionBlock tB=new TransactionBlock(a);
			DSObj.bChain.InsertBlock_Honest(tB);
			for(int j=0;j<DSObj.bChain.tr_count;j++){
				Pair<String, TransactionBlock> p=new Pair<String, TransactionBlock>(a[j].coinID,tB);
				a[j].Destination.mycoins.add(p);
			}
		}
		DSObj.latestCoinID=Integer.toString(coin-1);
	}
    
	public void initializeDSCoin(DSCoin_Malicious DSObj, int coinCount) {
		Members leader=new Members();
		leader.UID="Moderator";
		int tot_mem=DSObj.memberlist.length;
		int ind=0;
		int coin=100000;
		int no_tB=coinCount/DSObj.bChain.tr_count;
		for(int i=0;i<no_tB;i++){
			Transaction[] a=new Transaction[DSObj.bChain.tr_count];
			for(int j=0;j<DSObj.bChain.tr_count;j++){
				Transaction t=new Transaction();
				t.coinID=Integer.toString(coin);
				coin+=1;
				t.Source=leader;
				t.Destination=DSObj.memberlist[ind%tot_mem];
				ind+=1;
				a[j]=t;
			}
			TransactionBlock tB=new TransactionBlock(a);
			DSObj.bChain.InsertBlock_Malicious(tB);
			for(int j=0;j<DSObj.bChain.tr_count;j++){
				Pair<String, TransactionBlock> p=new Pair<String, TransactionBlock>(a[j].coinID,tB);
				a[j].Destination.mycoins.add(p);
			}
		}
		DSObj.latestCoinID=Integer.toString(coin-1);
	}
}
