//
// EvhConfAccountDTO.h
// generated at 2016-03-31 15:43:21 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

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

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

