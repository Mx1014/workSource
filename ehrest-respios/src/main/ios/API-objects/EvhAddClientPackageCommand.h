//
// EvhAddClientPackageCommand.h
// generated at 2016-04-01 15:40:23 
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

