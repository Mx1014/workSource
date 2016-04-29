//
// EvhEnterpriseContactDTO.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:57 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:55 
>>>>>>> 3.3.x
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhEnterpriseContactEntryDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhEnterpriseContactDTO
//
@interface EvhEnterpriseContactDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* enterpriseId;

@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSString* nickName;

@property(nonatomic, copy) NSString* avatar;

@property(nonatomic, copy) NSString* employeeNo;

@property(nonatomic, copy) NSString* groupName;

@property(nonatomic, copy) NSString* sex;

@property(nonatomic, copy) NSString* phone;

@property(nonatomic, copy) NSNumber* userId;

@property(nonatomic, copy) NSNumber* role;

@property(nonatomic, copy) NSNumber* status;

@property(nonatomic, copy) NSNumber* creatorUid;

@property(nonatomic, copy) NSNumber* createTime;

// item type EvhEnterpriseContactEntryDTO*
@property(nonatomic, strong) NSMutableArray* entries;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

