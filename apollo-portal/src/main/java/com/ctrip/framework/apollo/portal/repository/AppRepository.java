package com.ctrip.framework.apollo.portal.repository;

import com.ctrip.framework.apollo.common.entity.App;

import com.ctrip.framework.apollo.portal.entity.bo.SearchItemBO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Set;


public interface AppRepository extends PagingAndSortingRepository<App, Long> {

  App findByAppId(String appId);

  List<App> findByOwnerName(String ownerName, Pageable page);

  List<App> findByAppIdIn(Set<String> appIds);

  List<App> findByAppIdIn(Set<String> appIds, Pageable pageable);

  Page<App> findByAppIdContainingOrNameContaining(String appId, String name, Pageable pageable);

  @Query(value = "select B.AppId as appId,B.NamespaceName as namespaceName,A.Key as 'key' from ApolloConfigDB.Item A inner join ApolloConfigDB.Namespace B on A.IsDeleted = 0 and A.Key like ?1 and A.NamespaceId=B.Id",
           nativeQuery = true)
  List<Object[]> findItemsByKey(String key);

  @Query(value = "select B.AppId as appId,B.NamespaceName as namespaceName,A.Key as 'key' from ApolloConfigDB.Item A inner join ApolloConfigDB.Namespace B on A.IsDeleted = 0 and B.AppId = ?1 and A.Key like ?2 and A.NamespaceId=B.Id",
           nativeQuery = true)
  List<Object[]> findItemsByAppIdKey(String appid, String key);

  @Modifying
  @Query("UPDATE App SET IsDeleted=1,DataChange_LastModifiedBy = ?2 WHERE AppId=?1")
  int deleteApp(String appId, String operator);
}
