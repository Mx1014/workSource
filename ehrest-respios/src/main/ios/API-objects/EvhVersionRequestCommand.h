//
// EvhVersionRequestCommand.h
// generated at 2016-04-19 13:40:00 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhVersionDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhVersionRequestCommand
//
@interface EvhVersionRequestCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* realm;

@property(nonatomic, strong) EvhVersionDTO* currentVersion;

@property(nonatomic, copy) NSString* locale;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

