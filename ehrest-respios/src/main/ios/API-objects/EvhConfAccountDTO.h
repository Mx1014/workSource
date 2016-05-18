//
// EvhConfAccountDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhConfCategoryDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhConfAccountDTO
//
@interface EvhConfAccountDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* userId;

@property(nonatomic, copy) NSString* userName;

@property(nonatomic, copy) NSString* mobile;

@property(nonatomic, copy) NSString* enterpriseName;

@property(nonatomic, copy) NSString* department;

@property(nonatomic, copy) NSNumber* confType;

@property(nonatomic, copy) NSNumber* accountType;

@property(nonatomic, copy) NSNumber* updateDate;

@property(nonatomic, copy) NSNumber* validDate;

@property(nonatomic, copy) NSNumber* userType;

@property(nonatomic, copy) NSNumber* validFlag;

@property(nonatomic, copy) NSNumber* status;

@property(nonatomic, strong) EvhConfCategoryDTO* category;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

