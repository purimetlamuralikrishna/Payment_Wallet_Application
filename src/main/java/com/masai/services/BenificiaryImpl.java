package com.masai.services;




import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.masai.exceptions.NotFoundException;
import com.masai.exceptions.UserAlreadyExistWithMobileNumber;
import com.masai.model.Beneficiary;
import com.masai.model.Wallet;
import com.masai.repository.BankAccountDao;
import com.masai.repository.BeneficiaryDao;
import com.masai.repository.CustomerDao;
import com.masai.repository.WalletDao;
import com.masai.util.GetCurrentLoginUserSessionDetailsIntr;

@Component
public class BenificiaryImpl implements BeneficiaryIntr {

	
	@Autowired
	WalletDao wDao;
	@Autowired
	CustomerDao cDao;
	@Autowired
	BankAccountDao bDao;
	
	@Autowired
	BeneficiaryDao bfDao;

	
	@Autowired
    private GetCurrentLoginUserSessionDetailsIntr getCurrentLoginUser;
  
	@Override
	public Beneficiary addBenificiary(Beneficiary benificary, String key) {
		
		
		Wallet wallet = getCurrentLoginUser.getCurrentUserWallet(key);
		
		List<Beneficiary> benificiaryList = wallet.getBenificiaryList();
		
		
		 for(Beneficiary benificir:benificiaryList) {
			 
			 if(benificir.getMobileNumber().equals(benificary.getMobileNumber())) {
				 throw new UserAlreadyExistWithMobileNumber("Benificiary already added..");
			 }
		 }
		 
		benificiaryList.add(benificary);
		wDao.save(wallet);
		
		
		return benificary;
	}



	@Override
	public List<Beneficiary> viewAllBenificiary(String key) {
		Wallet wallet = getCurrentLoginUser.getCurrentUserWallet(key);
		List<Beneficiary> bfList = wallet.getBenificiaryList();
		if( bfList.size()<=0 ) 
			throw new NotFoundException("No Benificiaries details available..");
		return bfList;
	}



	@Override
	public Beneficiary removeBenificiary(String mobileNo , String key) {
		
		
		
Wallet wallet = getCurrentLoginUser.getCurrentUserWallet(key);
		
		List<Beneficiary> benificiaryList = wallet.getBenificiaryList();
		
		int index = 0;
		
		for(Beneficiary benificiary:benificiaryList) {
			
			 if(benificiary.getMobileNumber().equals(mobileNo)) {
				 
				 benificiaryList.remove(index);
				 bfDao.delete(benificiary);
				 wDao.save(wallet);
				 return benificiary;
			 }
			 
			 index++;
		 }
		 
		
		
		throw new UserAlreadyExistWithMobileNumber("No Benificiary found with given mobils number..");
		
		
	}
	
	
	
	
}
