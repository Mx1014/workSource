//
// EvhEnterpriseWithoutVideoConfAccountDTO.h
// generated at 2016-04-08 20:09:23 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhEnterpriseWithoutVideoConfAccountDTO
//
@interface EvhEnterpriseWithoutVideoConfAccountDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* enterpriseId;

@property(nonatomic, copy) NSString* enterpriseName;

@property(nonatomic, copy) NSString* enterpriseContactor;

@property(nonatomic, copy) NSString* mobile;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

