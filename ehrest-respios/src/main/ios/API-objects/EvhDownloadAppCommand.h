//
// EvhDownloadAppCommand.h
// generated at 2016-04-07 17:57:43 
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

