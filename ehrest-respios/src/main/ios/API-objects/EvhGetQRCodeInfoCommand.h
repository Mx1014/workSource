//
// EvhGetQRCodeInfoCommand.h
// generated at 2016-03-25 09:26:39 
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

