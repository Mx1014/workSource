//
// EvhUpdateCategoryLogoUriCommand.h
// generated at 2016-04-19 12:41:54 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUpdateCategoryLogoUriCommand
//
@interface EvhUpdateCategoryLogoUriCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSString* logoUri;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

