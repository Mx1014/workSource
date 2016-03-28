//
// EvhWithoutCurrentVersionRequestCommand.h
// generated at 2016-03-25 19:05:20 
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

