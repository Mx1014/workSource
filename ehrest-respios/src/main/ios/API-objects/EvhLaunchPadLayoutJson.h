//
// EvhLaunchPadLayoutJson.h
<<<<<<< HEAD
// generated at 2016-04-18 14:48:52 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhLaunchPadLayoutGroupDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhLaunchPadLayoutJson
//
@interface EvhLaunchPadLayoutJson
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* versionCode;

@property(nonatomic, copy) NSString* versionName;

@property(nonatomic, copy) NSString* layoutName;

@property(nonatomic, copy) NSString* displayName;

// item type EvhLaunchPadLayoutGroupDTO*
@property(nonatomic, strong) NSMutableArray* groups;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

