//
// EvhCreateLaunchPadLayoutAdminCommand.h
// generated at 2016-03-25 15:57:22 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateLaunchPadLayoutAdminCommand
//
@interface EvhCreateLaunchPadLayoutAdminCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* namespaceId;

@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSString* layoutJson;

@property(nonatomic, copy) NSNumber* versionCode;

@property(nonatomic, copy) NSNumber* minVersionCode;

@property(nonatomic, copy) NSNumber* status;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

