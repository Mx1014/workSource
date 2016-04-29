//
// EvhCreateLaunchPadLayoutAdminCommand.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:52 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:55 
>>>>>>> 3.3.x
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

