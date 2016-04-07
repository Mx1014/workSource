//
// EvhGetQRCodeInfoCommand.h
// generated at 2016-04-07 10:47:32 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetQRCodeInfoCommand
//
@interface EvhGetQRCodeInfoCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* qrid;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

