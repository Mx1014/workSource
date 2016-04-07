//
// EvhUpdateEnterpriseVideoConfAccountCommand.h
// generated at 2016-04-07 17:57:41 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUpdateEnterpriseVideoConfAccountCommand
//
@interface EvhUpdateEnterpriseVideoConfAccountCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* enterpriseId;

@property(nonatomic, copy) NSString* enterpriseContactor;

@property(nonatomic, copy) NSString* mobile;

@property(nonatomic, copy) NSNumber* status;

@property(nonatomic, copy) NSNumber* namespaceId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

