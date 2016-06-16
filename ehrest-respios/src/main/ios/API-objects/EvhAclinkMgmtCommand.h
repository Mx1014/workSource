//
// EvhAclinkMgmtCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAclinkMgmtCommand
//
@interface EvhAclinkMgmtCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* wifiSsid;

@property(nonatomic, copy) NSString* wifiPwd;

@property(nonatomic, copy) NSNumber* doorId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

