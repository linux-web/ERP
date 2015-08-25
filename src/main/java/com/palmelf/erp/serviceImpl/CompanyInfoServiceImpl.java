package com.palmelf.erp.serviceImpl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.palmelf.erp.dao.PublicDao;
import com.palmelf.erp.model.CompanyInfo;
import com.palmelf.erp.service.CompanyInfoService;
import com.palmelf.erp.shiro.ShiroUser;
import com.palmelf.erp.util.Constants;
import com.palmelf.erp.util.PageUtil;

/**
* 类功能说明 TODO:公司信息处理service
* 类修改者
* 修改日期
* 修改说明
* <p>Title: CompanyInfoServiceImpl.java</p>
* <p>Description:福产流通科技</p>
* <p>Copyright: Copyright (c) 2006</p>
* <p>Company:福产流通科技有限公司</p>
* @author lsy 756514656@qq.com
* @date 2013-4-28 下午12:52:04
* @version V1.0
*/

@Service("companyInfoService")
public class CompanyInfoServiceImpl implements CompanyInfoService {

	private static final Logger logger = LoggerFactory.getLogger(CompanyInfoServiceImpl.class);

	private PublicDao<CompanyInfo> publicDao;

	@Autowired
	public void setPublicDao(PublicDao<CompanyInfo> publicDao) {
		this.publicDao = publicDao;
	}

	/* (非 Javadoc)
	* <p>Title: persistenceCompanyInfo</p>
	* <p>Description: 持久化CompanyInfo</p>
	* @param map
	* @return
	* @see com.palmelf.erp.service.CompanyInfoService#persistenceCompanyInfo(java.util.Map)
	*/
	@Override
	public boolean persistenceCompanyInfo(Map<String, List<CompanyInfo>> map) {
		this.addCompanyInfo(map.get("addList"));
		this.updCompanyInfo(map.get("updList"));
		this.delCompanyInfo(map.get("delList"));
		return true;
	}

	@Override
	public boolean addCompanyInfo(List<CompanyInfo> addlist) {

		logger.debug("addCompanyInfo");
		if (addlist != null && addlist.size() != 0) {
			ShiroUser users = Constants.getCurrendUser();
			for (CompanyInfo companyInfo : addlist) {
				companyInfo.setCreated(new Date());
				companyInfo.setLastmod(new Date());
				companyInfo.setStatus("A");
				if (users != null) {
					companyInfo.setCreater(users.getUserId());
					companyInfo.setModifyer(users.getUserId());
				}
				publicDao.save(companyInfo);
			}
		}
		return true;
	}

	@Override
	public boolean updCompanyInfo(List<CompanyInfo> updlist) {
		logger.debug("updCompanyInfo");
		if (updlist != null && updlist.size() != 0) {
			ShiroUser users = Constants.getCurrendUser();
			for (CompanyInfo companyInfo : updlist) {
				companyInfo.setLastmod(new Date());
				companyInfo.setModifyer(users.getUserId());
				publicDao.update(companyInfo);
			}
		}
		return true;
	}

	@Override
	public boolean delCompanyInfo(List<CompanyInfo> dellist) {
		logger.debug("delCompanyInfo");
		if (dellist != null && dellist.size() != 0) {
			for (CompanyInfo companyInfo : dellist) {
				companyInfo.setStatus("I");
				companyInfo.setLastmod(new Date());
				publicDao.deleteToUpdate(companyInfo);
			}
		}
		return true;
	}

	@Override
	public boolean delCompanyInfo(Integer companyId) {
		CompanyInfo companyInfo = publicDao.get(CompanyInfo.class, companyId);
		companyInfo.setStatus("I");
		companyInfo.setLastmod(new Date());
		publicDao.deleteToUpdate(companyInfo);
		return true;
	}

	/* (非 Javadoc)
	* <p>Title: findAllCompanyInfoList</p>
	* <p>Description: 查询所有CompanyInfo</p>
	* @param param
	* @param page
	* @param rows
	* @return
	* @see com.palmelf.erp.service.CompanyInfoService#findAllCompanyInfoList(java.util.Map, java.lang.Integer, java.lang.Integer)
	*/
	@Override
	public List<CompanyInfo> findAllCompanyInfoList(Map<String, Object> param, PageUtil pageUtil) {
		String hql = "from CompanyInfo t where t.status='A' ";
		hql += Constants.getSearchConditionsHQL("t", param);
		hql += Constants.getGradeSearchConditionsHQL("t", pageUtil);
		return publicDao.find(hql, param, pageUtil.getPage(), pageUtil.getRows());
	}

	/* (非 Javadoc)
	* <p>Title: getCount</p>
	* <p>Description:查询条数 </p>
	* @param param
	* @return
	* @see com.palmelf.erp.service.CompanyInfoService#getCount(java.util.Map)
	*/
	@Override
	public Long getCount(Map<String, Object> param, PageUtil pageUtil) {
		String hql = "select count(*) from CompanyInfo t where t.status='A' ";
		hql += Constants.getSearchConditionsHQL("t", param);
		hql += Constants.getGradeSearchConditionsHQL("t", pageUtil);
		return publicDao.count(hql, param);
	}

}
