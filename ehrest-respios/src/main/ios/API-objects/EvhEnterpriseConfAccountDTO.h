//
// EvhEnterpriseConfAccountDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhEnterpriseConfAccountDTO
//
@interface EvhEnterpriseConfAccountDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* enterpriseId;

@property(nonatomic, copy) NSString* enterpriseName;

@property(nonatomic, copy) NSString* enterpriseDisplayName;

@property(nonatomic, copy) NSString* enterpriseContactor;

@property(nonatomic, copy) NSString* mobile;

@property(nonatomic, copy) NSNumber* useStatus;

@property(nonatomic, copy) NSNumber* status;

@property(nonatomic, copy) NSNumber* totalAccount;

@property(nonatomic, copy) NSNumber* validAccount;

@property(nonatomic, copy) NSNumber* buyChannel;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

