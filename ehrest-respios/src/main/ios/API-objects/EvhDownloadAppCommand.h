//
// EvhDownloadAppCommand.h
// generated at 2016-04-05 13:45:26 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDownloadAppCommand
//
@interface EvhDownloadAppCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* appType;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

