//
// EvhAclinkUpgradeResponse.h
// generated at 2016-04-19 12:41:53 
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

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

