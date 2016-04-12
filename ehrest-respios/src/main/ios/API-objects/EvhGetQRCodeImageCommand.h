//
// EvhGetQRCodeImageCommand.h
// generated at 2016-04-12 19:00:51 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetQRCodeImageCommand
//
@interface EvhGetQRCodeImageCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* qrid;

@property(nonatomic, copy) NSNumber* width;

@property(nonatomic, copy) NSNumber* height;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

