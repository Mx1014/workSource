//
// EvhEnterpriseWithoutVideoConfAccountDTO.h
// generated at 2016-03-25 17:08:12 
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

