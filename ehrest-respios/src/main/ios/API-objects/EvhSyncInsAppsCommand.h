//
// EvhSyncInsAppsCommand.h
// generated at 2016-04-06 19:59:46 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhAppInfo.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSyncInsAppsCommand
//
@interface EvhSyncInsAppsCommand
    : NSObject<EvhJsonSerializable>


// item type EvhAppInfo*
@property(nonatomic, strong) NSMutableArray* appInfos;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

