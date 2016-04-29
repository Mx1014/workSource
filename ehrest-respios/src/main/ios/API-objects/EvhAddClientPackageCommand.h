//
// EvhAddClientPackageCommand.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:50 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:53 
>>>>>>> 3.3.x
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAddClientPackageCommand
//
@interface EvhAddClientPackageCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSNumber* versionCode;

@property(nonatomic, copy) NSNumber* packageEdition;

@property(nonatomic, copy) NSNumber* devicePlatform;

@property(nonatomic, copy) NSNumber* distributionChannel;

@property(nonatomic, copy) NSString* tag;

@property(nonatomic, copy) NSString* jsonParams;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

