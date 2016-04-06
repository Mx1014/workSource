//
// EvhWithoutCurrentVersionRequestCommand.h
// generated at 2016-04-06 19:10:43 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhWithoutCurrentVersionRequestCommand
//
@interface EvhWithoutCurrentVersionRequestCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* realm;

@property(nonatomic, copy) NSString* locale;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

