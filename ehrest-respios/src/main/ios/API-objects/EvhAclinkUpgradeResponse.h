//
// EvhAclinkUpgradeResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAclinkUpgradeResponse
//
@interface EvhAclinkUpgradeResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSString* infoUrl;

@property(nonatomic, copy) NSString* downloadUrl;

@property(nonatomic, copy) NSNumber* creatorId;

@property(nonatomic, copy) NSString* message;

@property(nonatomic, copy) NSString* version;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

