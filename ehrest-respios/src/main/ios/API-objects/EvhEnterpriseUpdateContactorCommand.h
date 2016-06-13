//
// EvhEnterpriseUpdateContactorCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhEnterpriseUpdateContactorCommand
//
@interface EvhEnterpriseUpdateContactorCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* contactName;

@property(nonatomic, copy) NSString* entryValue;

@property(nonatomic, copy) NSNumber* enterpriseId;

@property(nonatomic, copy) NSNumber* communityId;

@property(nonatomic, copy) NSNumber* namespaceId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

