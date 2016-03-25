//
// EvhDownloadAppCommand.h
// generated at 2016-03-25 15:57:22 
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

