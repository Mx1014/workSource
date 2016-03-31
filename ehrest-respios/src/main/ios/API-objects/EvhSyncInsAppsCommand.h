//
// EvhSyncInsAppsCommand.h
// generated at 2016-03-31 11:07:25 
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

